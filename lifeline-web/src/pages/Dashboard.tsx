import React, { useState } from 'react';
import { useAppContext } from '../store/AppContext';
import { getSeverityColorClass, getSeverityBgClass, getSeverityBorderClass } from '../utils/styles';
import { generateActiveThreats } from '../store/PriorityEngine';
import clsx from 'clsx';
import { useNavigate } from 'react-router-dom';

function SensorCard({ label, value, unit, type, max = 100 }: { label: string, value: number, unit: string, type: keyof AppState['sensors'], max?: number }) {
  const { state } = useAppContext();
  // We can just rely on the activeThreats logic or a local severity helper
  const isHigh = value > max * 0.7; // Just a dummy bar calc
  const pct = Math.min(100, Math.max(0, (value / max) * 100));

  let status = 'NOMINAL';
  let colorClass = 'text-primary bg-primary border-outline-variant';
  
  const threat = state.activeThreats.find(t => t.type === type);
  if (threat) {
    status = threat.severity.toUpperCase();
    if (threat.severity === 'critical') colorClass = 'text-error bg-error border-error';
    else if (threat.severity === 'high') colorClass = 'text-error bg-error border-error'; // Or orange if we had it
    else colorClass = 'text-secondary bg-secondary border-secondary';
  }

  const [, bg, border] = colorClass.split(' ');

  return (
    <div className={clsx("bg-surface-container-low border p-3 flex flex-col gap-2", border)}>
      <div className="flex justify-between items-start">
        <span className="font-label-caps text-label-caps text-on-surface-variant uppercase">{label}</span>
        <span className={clsx("font-label-caps text-label-caps", colorClass.split(' ')[0])}>{status}</span>
      </div>
      <div className="flex items-baseline gap-1">
        <span className={clsx("font-data-lg text-data-lg", status === 'NOMINAL' ? 'text-primary' : colorClass.split(' ')[0])}>{value.toFixed(1)}</span>
        <span className="font-data-md text-data-md text-on-surface-variant">{unit}</span>
      </div>
      <div className="w-full h-1 bg-surface-variant">
        <div className={clsx("h-full", bg, status === 'CRITICAL' && 'animate-pulse')} style={{ width: `${pct}%` }}></div>
      </div>
    </div>
  );
}

export default function Dashboard() {
  const { state, dispatch } = useAppContext();
  const navigate = useNavigate();
  const [simulating, setSimulating] = useState(false);

  const highestSeverity = state.activeThreats.some(t => t.severity === 'critical') ? 'CRITICAL' :
                          state.activeThreats.some(t => t.severity === 'high') ? 'HIGH' :
                          state.activeThreats.length > 0 ? 'CAUTION' : 'NOMINAL';

  const handleSimulate = () => {
    if (simulating) return;
    setSimulating(true);
    let count = 0;
    
    dispatch({ type: 'ADD_LOG', payload: { type: 'alert', description: 'Simulated emergency initiated.' } });

    const interval = setInterval(() => {
      count++;
      dispatch({
        type: 'UPDATE_SENSORS',
        payload: {
          oxygen: Math.max(12.4, 95 - count * 5),
          pressure: Math.max(42.1, 101 - count * 4),
          radiation: Math.min(84.2, 15 + count * 5)
        }
      });
      if (count >= 15) {
        clearInterval(interval);
        setSimulating(false);
      }
    }, 1000);
  };

  return (
    <div className="flex flex-col px-margin-mobile md:px-margin-tablet py-4 gap-gutter w-full">
      <div className="flex justify-between items-center">
        <h1 className="font-display-lg text-display-lg-mobile text-primary">System Overview</h1>
        <button 
          onClick={handleSimulate}
          disabled={simulating}
          className="bg-surface-container-high border border-outline-variant px-3 py-1 font-label-caps text-label-caps hover:bg-surface-bright disabled:opacity-50"
        >
          {simulating ? 'SIMULATING...' : 'SIMULATE EMERGENCY'}
        </button>
      </div>

      {highestSeverity !== 'NOMINAL' && (
        <div className="w-full bg-error text-on-error p-3 flex items-center justify-between border border-error-container">
          <div className="flex items-center gap-2">
            <span className="material-symbols-outlined fill">warning</span>
            <span className="font-headline-md text-headline-md uppercase">Critical Failure</span>
          </div>
          <span className="font-label-caps text-label-caps">MULTIPLE THREATS DETECTED</span>
        </div>
      )}

      <div className="grid grid-cols-2 md:grid-cols-3 gap-unit">
        <SensorCard label="Radiation" value={state.sensors.radiation} unit="%" type="radiation" />
        <SensorCard label="Oxygen" value={state.sensors.oxygen} unit="%" type="oxygen" />
        <SensorCard label="Temp" value={state.sensors.temperature} unit="°C" type="temperature" max={50} />
        <SensorCard label="Power" value={state.sensors.power} unit="%" type="power" />
        <SensorCard label="Hull Press" value={state.sensors.pressure} unit="kPa" type="pressure" max={110} />
        <SensorCard label="Earth Sig" value={state.sensors.earthSignal} unit="dB" type="earthSignal" />
      </div>

      {state.currentPriorityAction && (
        <div className="bg-surface border border-outline-variant mt-4 flex flex-col">
          <div className="border-b border-outline-variant p-3 flex items-center justify-between bg-surface-container-low">
            <span className="font-label-caps text-label-caps text-primary">AI RECOMMENDED ACTION</span>
            <span className="font-label-caps text-label-caps text-error border border-error px-1">PRIORITY 1</span>
          </div>
          <div className="p-4 flex flex-col gap-4">
            <h3 className="font-display-lg-mobile text-display-lg-mobile text-primary">{state.currentPriorityAction.title}</h3>
            <p className="font-body-base text-body-base text-on-surface-variant font-data-md">
              &gt; {state.currentPriorityAction.reason.toUpperCase()} <br/>
              &gt; CONFIDENCE: {state.currentPriorityAction.confidence.toUpperCase()}<br/>
              &gt; ASSIGNED: {state.crew.find(c => c.id === state.currentPriorityAction?.assignedCrewId)?.name?.toUpperCase() || 'UNASSIGNED'}
            </p>
            <button 
              onClick={() => navigate('/procedures')}
              className="bg-primary text-on-primary font-label-caps text-label-caps p-4 w-full flex items-center justify-between hover:bg-surface-bright hover:text-primary transition-colors border border-primary"
            >
              <span>VIEW FULL PROCEDURE</span>
              <span className="material-symbols-outlined">arrow_forward</span>
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
