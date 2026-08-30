import React from 'react';
import { useAppContext } from '../store/AppContext';
import clsx from 'clsx';

export default function Crew() {
  const { state, dispatch } = useAppContext();

  // Simple hardcoded mapping for icons based on role
  const getIcon = (role: string) => {
    switch (role) {
      case 'Commander': return 'engineering'; // Or appropriate icon
      case 'Medical': return 'medical_services';
      case 'Pilot': return 'flight_takeoff';
      case 'Engineer': return 'construction';
      default: return 'person';
    }
  };

  return (
    <div className="flex flex-col px-margin-mobile md:px-margin-tablet py-6 gap-gutter w-full">
      <div className="w-full bg-surface-container-high border border-outline-variant px-4 py-2 flex items-center justify-between mb-4">
        <div className="flex items-center gap-2">
          <span className="material-symbols-outlined text-primary text-sm">wifi_tethering</span>
          <span className="font-label-caps text-label-caps text-on-surface uppercase">SYNCED VIA LOCAL MESH</span>
        </div>
        <div className="flex items-center gap-2">
          <span className="w-2 h-2 rounded-full bg-primary animate-pulse"></span>
          <span className="font-label-caps text-label-caps text-on-surface uppercase">{state.crew.length} DEVICES</span>
        </div>
      </div>

      <h2 className="font-label-caps text-label-caps text-on-surface uppercase px-1 mb-2">CREW ROSTER</h2>
      
      <div className="flex flex-col gap-4">
        {state.crew.map(member => {
          const isStressed = member.vitals.stressLevel === 'ELEVATED' || member.vitals.stressLevel === 'CRITICAL';
          
          return (
            <article key={member.id} className={clsx(
              "bg-surface-container-low border p-4 flex flex-col gap-4 relative overflow-hidden transition-all duration-300",
              isStressed ? 'border-error' : 'border-outline-variant',
              member.assignedTask && !isStressed ? 'border-primary shadow-[0_0_10px_rgba(255,255,255,0.1)]' : ''
            )}>
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div className="flex items-center gap-4">
                  <div className="w-10 h-10 bg-surface-bright border border-outline-variant flex items-center justify-center">
                    <span className={clsx("material-symbols-outlined", isStressed ? "text-error" : "text-primary")}>
                      {getIcon(member.role)}
                    </span>
                  </div>
                  <div>
                    <h3 className="font-headline-md text-headline-md text-primary">{member.name}</h3>
                    <p className="font-label-caps text-label-caps text-on-surface-variant uppercase">
                      {member.role} {member.assignedTask ? `- ${member.assignedTask}` : '- Standby'}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-6">
                  <div className="flex flex-col items-end">
                    <span className={clsx("font-label-caps text-label-caps uppercase mb-1", isStressed ? "text-error" : "text-on-surface-variant")}>HR</span>
                    <span className={clsx("font-data-md text-data-md", isStressed ? "text-error" : "text-primary")}>
                      {member.vitals.heartRate} <span className="text-on-surface-variant text-sm">bpm</span>
                    </span>
                  </div>
                  <div className="flex flex-col items-end">
                    <span className={clsx("font-label-caps text-label-caps uppercase mb-1", isStressed ? "text-error" : "text-on-surface-variant")}>STRESS</span>
                    <span className={clsx("font-data-md text-data-md", isStressed ? "text-error animate-pulse" : "text-primary")}>
                      {member.vitals.stressLevel}
                    </span>
                  </div>
                  <span className={clsx("w-3 h-3 rounded-full", isStressed ? "bg-error" : "bg-primary")}></span>
                </div>
              </div>

              {isStressed && member.assignedTask && (
                <div className="mt-2 bg-error-container/20 border border-error/50 p-3 flex items-center justify-between">
                  <div className="flex items-center gap-2">
                    <span className="material-symbols-outlined text-error text-sm">warning</span>
                    <span className="font-body-base text-body-base text-error">Reassign Task?</span>
                  </div>
                  <button className="bg-surface-bright text-on-surface px-4 py-2 font-label-caps text-label-caps uppercase border border-outline-variant hover:bg-surface transition-colors">
                    REASSIGN
                  </button>
                </div>
              )}
            </article>
          )
        })}
      </div>
    </div>
  );
}
