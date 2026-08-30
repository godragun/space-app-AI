import { Sensors, CrewMember, ActiveThreat, PriorityAction, ThreatSeverity } from './types';

// Ranking 1 = Highest Priority
const THREAT_CATEGORY_RANKING: Record<keyof Sensors, number> = {
  oxygen: 1, // Life support
  pressure: 2, // Life support / Hull
  radiation: 3, // Crew safety
  temperature: 4, // Life support secondary
  power: 5, // Systems
  earthSignal: 6, // Comms
};

const THREAT_ROLE_MAPPING: Record<keyof Sensors, CrewMember['role'][]> = {
  oxygen: ['Engineer', 'Commander', 'Pilot', 'Medical'],
  pressure: ['Engineer', 'Commander', 'Pilot', 'Medical'],
  radiation: ['Medical', 'Commander', 'Engineer', 'Pilot'],
  temperature: ['Engineer', 'Medical', 'Commander', 'Pilot'],
  power: ['Engineer', 'Pilot', 'Commander', 'Medical'],
  earthSignal: ['Pilot', 'Commander', 'Engineer', 'Medical'],
};

function getSensorSeverity(type: keyof Sensors, value: number): ThreatSeverity | 'normal' {
  switch (type) {
    case 'oxygen':
      if (value < 30) return 'critical';
      if (value < 50) return 'high';
      if (value < 70) return 'caution';
      return 'normal';
    case 'pressure':
      if (value < 50) return 'critical';
      if (value < 70) return 'high';
      if (value < 90) return 'caution';
      return 'normal';
    case 'radiation':
      if (value > 80) return 'critical';
      if (value > 60) return 'high';
      if (value > 40) return 'caution';
      return 'normal';
    case 'temperature':
      if (value > 40 || value < 5) return 'critical';
      if (value > 35 || value < 10) return 'high';
      if (value > 30 || value < 15) return 'caution';
      return 'normal';
    case 'power':
      if (value < 20) return 'critical';
      if (value < 40) return 'high';
      if (value < 60) return 'caution';
      return 'normal';
    case 'earthSignal':
      if (value < 5) return 'critical';
      if (value < 20) return 'high';
      if (value < 50) return 'caution';
      return 'normal';
    default:
      return 'normal';
  }
}

export function generateActiveThreats(sensors: Sensors): ActiveThreat[] {
  const threats: ActiveThreat[] = [];
  for (const [key, value] of Object.entries(sensors)) {
    const type = key as keyof Sensors;
    const severity = getSensorSeverity(type, value);
    if (severity !== 'normal') {
      threats.push({
        id: `threat-${type}`,
        type,
        severity,
        description: `${type.toUpperCase()} levels are at ${value} (${severity.toUpperCase()})`,
      });
    }
  }
  return threats;
}

export function calculatePriority(sensors: Sensors, crew: CrewMember[], currentThreats: ActiveThreat[]): PriorityAction | null {
  const newThreats = generateActiveThreats(sensors);
  // Merge or just use the generated ones based on current sensor values. 
  // For this deterministic engine, sensor values dictate the active threats directly.
  
  if (newThreats.length === 0) {
    return null;
  }

  // Filter to only high/critical for immediate action, or fallback to caution
  let actionableThreats = newThreats.filter(t => t.severity === 'critical' || t.severity === 'high');
  if (actionableThreats.length === 0) {
    actionableThreats = newThreats; // Only cautions exist
  }

  // Sort by severity (critical first) then by category ranking
  actionableThreats.sort((a, b) => {
    if (a.severity === 'critical' && b.severity !== 'critical') return -1;
    if (a.severity !== 'critical' && b.severity === 'critical') return 1;
    if (a.severity === 'high' && b.severity === 'caution') return -1;
    if (a.severity === 'caution' && b.severity === 'high') return 1;
    
    return THREAT_CATEGORY_RANKING[a.type] - THREAT_CATEGORY_RANKING[b.type];
  });

  const topThreat = actionableThreats[0];
  
  // Assign Crew
  const preferredRoles = THREAT_ROLE_MAPPING[topThreat.type];
  let assignedCrew: CrewMember | null = null;
  
  for (const role of preferredRoles) {
    // Find all crew with this role
    const candidates = crew.filter(c => c.role === role);
    // Find one without elevated/critical stress
    const healthyCandidate = candidates.find(c => c.vitals.stressLevel === 'LOW' || c.vitals.stressLevel === 'MIN');
    if (healthyCandidate) {
      assignedCrew = healthyCandidate;
      break;
    }
  }

  // Fallback: Just assign to whoever is healthiest if role matching fails (or if everyone is stressed, pick least stressed)
  if (!assignedCrew) {
    const availableCrew = [...crew].sort((a, b) => {
      const stressScore = { MIN: 0, LOW: 1, ELEVATED: 2, CRITICAL: 3 };
      return stressScore[a.vitals.stressLevel] - stressScore[b.vitals.stressLevel];
    });
    assignedCrew = availableCrew[0];
  }

  let title = '';
  switch(topThreat.type) {
    case 'oxygen': title = 'Restore Oxygen / Patch Leak'; break;
    case 'pressure': title = 'Seal Hull Breach'; break;
    case 'radiation': title = 'Deploy Radiation Shielding / Evacuate Sector'; break;
    case 'temperature': title = 'Regulate Thermal Systems'; break;
    case 'power': title = 'Restore Primary Power'; break;
    case 'earthSignal': title = 'Re-align Comms Array'; break;
  }

  const confidence = newThreats.length > 2 ? 'Medium' : 'High'; // Simple heuristic

  return {
    id: `action-${Date.now()}`,
    title,
    reason: `${topThreat.type.charAt(0).toUpperCase() + topThreat.type.slice(1)} has reached ${topThreat.severity} levels (${sensors[topThreat.type]}). This outranks other active issues.`,
    confidence,
    assignedCrewId: assignedCrew ? assignedCrew.id : null,
    threatId: topThreat.id
  };
}
