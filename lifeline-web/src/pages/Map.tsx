import React from 'react';
import { useAppContext } from '../store/AppContext';
import clsx from 'clsx';

export default function Map() {
  const { state } = useAppContext();

  // Basic sector mapping based on threat type
  const threatSectors: Record<string, string> = {
    oxygen: 'Sector 4',
    pressure: 'Sector 4',
    radiation: 'Sector 2',
    temperature: 'Sector 1',
    power: 'Sector 3',
    earthSignal: 'Comms Array',
  };

  const activeSectors = state.activeThreats.map(t => threatSectors[t.type] || 'Unknown');

  return (
    <div className="flex flex-col h-[calc(100vh-8rem)] w-full max-w-container-max mx-auto px-margin-mobile py-4 gap-unit relative">
      <div className="bg-error-container border border-error p-3 z-10 flex justify-between items-start md:items-center flex-col md:flex-row gap-2 shrink-0">
        <div>
          <span className="font-label-caps text-label-caps text-error block mb-1">MAP OVERVIEW</span>
          <span className="font-data-md text-data-md text-on-error-container">
            {activeSectors.length > 0 ? `THREATS IN: ${Array.from(new Set(activeSectors)).join(', ')}` : 'ALL SECTORS NOMINAL'}
          </span>
        </div>
      </div>

      <div className="relative flex-1 border border-outline-variant bg-[#0b0e14] overflow-hidden flex items-center justify-center min-h-[300px]" style={{
        backgroundImage: `linear-gradient(to right, rgba(51, 65, 85, 0.2) 1px, transparent 1px), linear-gradient(to bottom, rgba(51, 65, 85, 0.2) 1px, transparent 1px)`,
        backgroundSize: '20px 20px'
      }}>
        
        {/* Mock Map Layout */}
        <div className="absolute top-[20%] left-[20%] w-[30%] h-[30%] border border-outline-variant flex items-center justify-center text-on-surface-variant font-label-caps">Sector 1</div>
        <div className="absolute top-[20%] left-[50%] w-[30%] h-[30%] border border-outline-variant flex items-center justify-center text-on-surface-variant font-label-caps">Sector 2</div>
        <div className="absolute top-[50%] left-[20%] w-[30%] h-[30%] border border-outline-variant flex items-center justify-center text-on-surface-variant font-label-caps">Sector 3</div>
        <div className="absolute top-[50%] left-[50%] w-[30%] h-[30%] border border-outline-variant flex items-center justify-center text-on-surface-variant font-label-caps">Sector 4</div>

        {/* Threat Markers */}
        {state.activeThreats.map(threat => {
          let top = '50%', left = '50%';
          if (threatSectors[threat.type] === 'Sector 1') { top = '35%'; left = '35%'; }
          if (threatSectors[threat.type] === 'Sector 2') { top = '35%'; left = '65%'; }
          if (threatSectors[threat.type] === 'Sector 3') { top = '65%'; left = '35%'; }
          if (threatSectors[threat.type] === 'Sector 4') { top = '65%'; left = '65%'; }

          return (
            <div key={threat.id} className="absolute flex flex-col items-center" style={{ top, left, transform: 'translate(-50%, -50%)' }}>
              <div className="w-4 h-4 bg-error rounded-full animate-pulse border-2 border-on-error-container shadow-[0_0_15px_rgba(255,180,171,0.8)]"></div>
              <span className="mt-1 bg-surface-container/80 border border-error text-error font-label-caps text-label-caps px-1 py-0.5 backdrop-blur-sm">
                {threat.type.toUpperCase()}
              </span>
            </div>
          )
        })}

        {/* Crew Markers */}
        {state.crew.map((member, i) => {
          const top = `${40 + i * 10}%`;
          const left = `${40 + (i % 2) * 20}%`;
          return (
            <div key={member.id} className="absolute flex flex-col items-center" style={{ top, left }}>
              <div className="w-3 h-3 bg-secondary rounded-full border border-on-secondary shadow-[0_0_10px_rgba(183,200,225,0.6)]"></div>
              <span className="mt-1 bg-surface-container/80 border border-secondary text-secondary font-label-caps text-[10px] px-1 py-0.5 backdrop-blur-sm">
                {member.name.toUpperCase()}
              </span>
            </div>
          );
        })}
      </div>
    </div>
  );
}
