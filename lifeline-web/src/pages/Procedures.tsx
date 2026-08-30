import React from 'react';
import { useAppContext } from '../store/AppContext';
import clsx from 'clsx';

export default function Procedures() {
  const { state, dispatch } = useAppContext();

  const handleComplete = () => {
    if (state.currentPriorityAction) {
      dispatch({ 
        type: 'MARK_TASK_COMPLETE', 
        payload: { 
          actionId: state.currentPriorityAction.id, 
          threatId: state.currentPriorityAction.threatId 
        } 
      });
    }
  };

  if (!state.currentPriorityAction) {
    return (
      <div className="p-margin-mobile md:p-margin-tablet">
        <h1 className="font-display-lg text-primary mb-4">Procedures</h1>
        <p className="text-on-surface-variant">All systems nominal. No active procedures required.</p>
      </div>
    );
  }

  return (
    <div className="flex flex-col px-margin-mobile md:px-margin-tablet py-6 gap-gutter w-full">
      <div className="bg-[#161920] border-2 border-error p-6 rounded-none shadow-[0_0_15px_rgba(255,180,171,0.1)]">
        <div className="flex justify-between items-center mb-6 border-b border-outline-variant pb-2">
          <span className="font-label-caps text-label-caps text-error flex items-center gap-2">
            <span className="material-symbols-outlined text-[16px]">priority_high</span>
            CURRENT OBJECTIVE
          </span>
          <span className="font-data-md text-data-md text-on-surface-variant">PRIORITY 1</span>
        </div>
        
        <h2 className="font-display-lg-mobile md:font-display-lg text-primary mb-8 leading-tight">
          {state.currentPriorityAction.title}
        </h2>
        
        <p className="font-body-base text-on-surface-variant mb-8">
          Reason: {state.currentPriorityAction.reason}
        </p>

        <button 
          onClick={handleComplete}
          className="w-full bg-primary text-on-primary font-label-caps text-label-caps py-4 rounded-none flex items-center justify-center gap-2 hover:bg-surface-bright hover:text-primary border border-primary transition-colors active:scale-[0.98]"
        >
          <span className="material-symbols-outlined text-[20px] fill">check_circle</span>
          MARK COMPLETE
        </button>
      </div>
    </div>
  );
}
