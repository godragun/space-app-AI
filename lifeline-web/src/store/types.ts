export type ConnectionStatus = "connected" | "delayed" | "offline";

export interface Sensors {
  radiation: number; // 0-100%
  oxygen: number; // 0-100%
  temperature: number; // celsius
  power: number; // 0-100%
  pressure: number; // kPa
  earthSignal: number; // 0-100 dB or %
}

export type CrewRole = "Commander" | "Engineer" | "Medical" | "Pilot";

export interface CrewMember {
  id: string;
  name: string;
  role: CrewRole;
  vitals: {
    heartRate: number;
    stressLevel: "MIN" | "LOW" | "ELEVATED" | "CRITICAL";
  };
  assignedTask: string | null;
  taskStatus: "pending" | "complete" | null;
}

export type ThreatSeverity = "caution" | "high" | "critical";

export interface ActiveThreat {
  id: string;
  type: keyof Sensors;
  severity: ThreatSeverity;
  description: string;
}

export interface PriorityAction {
  id: string;
  title: string;
  reason: string;
  confidence: "High" | "Medium" | "Low";
  assignedCrewId: string | null;
  threatId: string | null;
}

export type LogEntryType = "system" | "task" | "sync" | "routine" | "alert";

export interface ActionLogEntry {
  id: string;
  timestamp: number;
  type: LogEntryType;
  description: string;
}

export interface AppState {
  connectionStatus: ConnectionStatus;
  sensors: Sensors;
  crew: CrewMember[];
  activeThreats: ActiveThreat[];
  currentPriorityAction: PriorityAction | null;
  actionLog: ActionLogEntry[];
}
