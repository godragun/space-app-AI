import React from 'react';
import { useAppContext } from '../store/AppContext';
import clsx from 'clsx';

export default function Log() {
  const { state } = useAppContext();

  const getIcon = (type: string) => {
    switch (type) {
      case 'alert': return 'warning';
      case 'task': return 'task_alt';
      case 'sync': return 'sync';
      case 'routine': return 'description';
      default: return 'info';
    }
  };

  const getColorClass = (type: string) => {
    switch (type) {
      case 'alert': return 'text-error border-error bg-error/20';
      case 'sync': return 'text-secondary border-secondary bg-secondary/10';
      default: return 'text-on-surface-variant border-outline-variant bg-surface-variant/50';
    }
  };

  const handleReport = () => {
    alert("REPORT GENERATED:\n\n" + state.actionLog.map(l => `[${new Date(l.timestamp).toISOString()}] ${l.description}`).join('\n'));
  };

  return (
    <div className="flex flex-col px-margin-mobile md:px-margin-tablet py-6 gap-gutter w-full">
      <div className="w-full bg-surface-variant text-on-surface p-4 mb-4 flex items-center justify-between border-l-4 border-secondary-container">
        <div className="flex items-center gap-3">
          <span className="material-symbols-outlined text-secondary">
            {state.connectionStatus === 'connected' ? 'cloud_done' : 'cloud_off'}
          </span>
          <span className="font-label-caps text-label-caps tracking-widest text-secondary">
            Sync Status: {state.connectionStatus === 'connected' ? 'Synced with Earth' : 'Queued for Earth (Offline)'}
          </span>
        </div>
        <button onClick={handleReport} className="bg-primary text-on-primary font-label-caps text-label-caps px-4 py-2 hover:bg-surface-bright hover:text-primary transition-colors border border-primary">
          Generate Report
        </button>
      </div>

      <h1 className="font-display-lg text-display-lg-mobile mb-6 font-bold tracking-tight">Mission Log</h1>

      <div className="flex flex-col gap-4">
        {state.actionLog.length === 0 && (
          <div className="text-on-surface-variant font-data-md">No log entries yet.</div>
        )}
        {state.actionLog.map(log => {
          const colorClass = getColorClass(log.type);
          
          return (
            <div key={log.id} className={clsx("bg-[#161920] border p-4 flex flex-col md:flex-row md:items-center gap-4 relative", log.type === 'alert' ? 'border-error' : 'border-outline-variant')}>
              <div className={clsx("absolute top-2 right-2 font-label-caps text-[10px] px-1 py-0.5 border", colorClass.split(' ')[0], colorClass.split(' ')[1])}>
                {log.type.toUpperCase()}
              </div>
              <div className="font-data-md text-data-md text-on-surface-variant w-40 shrink-0">
                {new Date(log.timestamp).toISOString().replace('T', '\n').replace('Z', 'Z')}
              </div>
              <div className={clsx("flex items-center justify-center w-10 h-10 rounded-full shrink-0", colorClass.split(' ')[2])}>
                <span className={clsx("material-symbols-outlined", colorClass.split(' ')[0])}>{getIcon(log.type)}</span>
              </div>
              <div className="flex-grow">
                <div className={clsx("font-data-md text-data-md text-on-surface")}>{log.description}</div>
              </div>
            </div>
          )
        })}
      </div>
    </div>
  );
}
