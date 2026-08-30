import React, { createContext, useContext, useEffect, useReducer, ReactNode } from 'react';
import { AppState, Sensors, ConnectionStatus, ActionLogEntry } from './types';
import { calculatePriority, generateActiveThreats } from './PriorityEngine';

const INITIAL_STATE: AppState = {
  connectionStatus: 'connected',
  sensors: {
    radiation: 15,
    oxygen: 95,
    temperature: 22,
    power: 90,
    pressure: 101,
    earthSignal: 100,
  },
  crew: [
    { id: '1', name: 'Cmdr. Vance', role: 'Commander', vitals: { heartRate: 72, stressLevel: 'LOW' }, assignedTask: null, taskStatus: null },
    { id: '2', name: 'Dr. Aris', role: 'Medical', vitals: { heartRate: 65, stressLevel: 'MIN' }, assignedTask: null, taskStatus: null },
    { id: '3', name: 'Lt. Reyes', role: 'Pilot', vitals: { heartRate: 68, stressLevel: 'MIN' }, assignedTask: null, taskStatus: null },
    { id: '4', name: 'Eng. Chen', role: 'Engineer', vitals: { heartRate: 75, stressLevel: 'LOW' }, assignedTask: null, taskStatus: null },
  ],
  activeThreats: [],
  currentPriorityAction: null,
  actionLog: [],
};

type Action = 
  | { type: 'UPDATE_SENSORS'; payload: Partial<Sensors> }
  | { type: 'MARK_TASK_COMPLETE'; payload: { actionId: string; threatId: string | null } }
  | { type: 'SET_CONNECTION'; payload: ConnectionStatus }
  | { type: 'ADD_LOG'; payload: Omit<ActionLogEntry, 'id'> }
  | { type: 'SYNC_STATE'; payload: AppState };

function appReducer(state: AppState, action: Action): AppState {
  switch (action.type) {
    case 'SYNC_STATE':
      return action.payload;
    case 'UPDATE_SENSORS': {
      const newSensors = { ...state.sensors, ...action.payload };
      const newThreats = generateActiveThreats(newSensors);
      
      // Update crew vitals based on stress (simulate stress on emergency)
      const hasCritical = newThreats.some(t => t.severity === 'critical');
      const newCrew = state.crew.map(c => {
        if (hasCritical && c.role === 'Medical') {
          return { ...c, vitals: { ...c.vitals, heartRate: 135, stressLevel: 'CRITICAL' as const } };
        }
        return c;
      });

      const nextAction = calculatePriority(newSensors, newCrew, newThreats);
      
      // Assign task to crew
      const crewWithAssignments = newCrew.map(c => {
        if (nextAction && c.id === nextAction.assignedCrewId) {
          return { ...c, assignedTask: nextAction.title, taskStatus: 'pending' as const };
        }
        return { ...c, assignedTask: null, taskStatus: null };
      });

      return {
        ...state,
        sensors: newSensors,
        activeThreats: newThreats,
        currentPriorityAction: nextAction,
        crew: crewWithAssignments,
      };
    }
    case 'MARK_TASK_COMPLETE': {
      // Find the threat and reset the sensor to a nominal value to "resolve" it
      const { threatId } = action.payload;
      const newSensors = { ...state.sensors };
      
      if (threatId === 'threat-oxygen') newSensors.oxygen = 95;
      if (threatId === 'threat-pressure') newSensors.pressure = 101;
      if (threatId === 'threat-radiation') newSensors.radiation = 15;
      if (threatId === 'threat-temperature') newSensors.temperature = 22;
      if (threatId === 'threat-power') newSensors.power = 90;
      if (threatId === 'threat-earthSignal') newSensors.earthSignal = 100;

      const newThreats = generateActiveThreats(newSensors);
      
      // Reset crew stress if no critical threats
      const hasCritical = newThreats.some(t => t.severity === 'critical');
      const newCrew = state.crew.map(c => {
        if (!hasCritical && c.role === 'Medical') {
          return { ...c, vitals: { ...c.vitals, heartRate: 75, stressLevel: 'LOW' as const } };
        }
        return c;
      });

      const nextAction = calculatePriority(newSensors, newCrew, newThreats);
      
      const crewWithAssignments = newCrew.map(c => {
        if (nextAction && c.id === nextAction.assignedCrewId) {
          return { ...c, assignedTask: nextAction.title, taskStatus: 'pending' as const };
        }
        return { ...c, assignedTask: null, taskStatus: null };
      });

      return {
        ...state,
        sensors: newSensors,
        activeThreats: newThreats,
        currentPriorityAction: nextAction,
        crew: crewWithAssignments,
        actionLog: [
          {
            id: `log-${Date.now()}`,
            timestamp: Date.now(),
            type: 'task',
            description: `Completed priority task: ${state.currentPriorityAction?.title}`,
          },
          ...state.actionLog
        ]
      };
    }
    case 'SET_CONNECTION':
      return { ...state, connectionStatus: action.payload };
    case 'ADD_LOG':
      return {
        ...state,
        actionLog: [{ ...action.payload, id: `log-${Date.now()}` }, ...state.actionLog],
      };
    default:
      return state;
  }
}

interface AppContextType {
  state: AppState;
  dispatch: React.Dispatch<Action>;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

const BROADCAST_CHANNEL_NAME = 'lifeline-mesh-sync';

export const AppProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(appReducer, INITIAL_STATE);

  useEffect(() => {
    const channel = new BroadcastChannel(BROADCAST_CHANNEL_NAME);
    
    channel.onmessage = (event) => {
      if (event.data.type === 'SYNC_STATE_FROM_MESH') {
        dispatch({ type: 'SYNC_STATE', payload: event.data.payload });
      }
    };

    return () => channel.close();
  }, []);

  // Sync state to mesh whenever it changes locally
  const dispatchWithSync = (action: Action) => {
    dispatch(action);
    // Let the reducer process, then broadcast the new state
    // In a real app we'd broadcast the action or compute next state, 
    // here we use a timeout to let React finish the render cycle and then read the state.
    // A better way is a custom middleware. For simplicity:
    setTimeout(() => {
       // We can't access updated state here easily due to closure.
       // We'll use an effect below to sync whenever state changes.
    }, 0);
  };

  return (
    <AppContext.Provider value={{ state, dispatch: dispatchWithSync }}>
      <SyncManager state={state} />
      {children}
    </AppContext.Provider>
  );
};

// Helper component to broadcast state changes
const SyncManager: React.FC<{ state: AppState }> = ({ state }) => {
  useEffect(() => {
    const channel = new BroadcastChannel(BROADCAST_CHANNEL_NAME);
    channel.postMessage({ type: 'SYNC_STATE_FROM_MESH', payload: state });
    return () => channel.close();
  }, [state]);
  return null;
};

export const useAppContext = () => {
  const context = useContext(AppContext);
  if (!context) throw new Error('useAppContext must be used within AppProvider');
  return context;
};
