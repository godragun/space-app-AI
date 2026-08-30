import React, { useState } from 'react';
import { useAppContext } from '../store/AppContext';
import clsx from 'clsx';

const KNOWLEDGE_BASE = [
  { q: 'diagnostic on secondary o2', a: 'Standard flow rate: 12.5 L/min. If flow drops below 10.0 L/min, check valve C-4 for obstruction.' },
  { q: 'valve c-4 clear. pressure is oscillating.', a: 'Oscillating pressure with clear valves suggests regulator fatigue. RECOMMENDATION: Isolate secondary loop and switch to emergency bypass until maintenance possible.', ai: true, conf: '68%' },
];

export default function Assistant() {
  const { state } = useAppContext();
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([
    { sender: 'user', text: 'Diagnostic on Secondary O2 flow rate.', type: 'normal' },
    { sender: 'assistant', text: KNOWLEDGE_BASE[0].a, type: 'known' },
  ]);

  const handleSend = () => {
    if (!input.trim()) return;
    
    const userMsg = input.trim();
    setMessages(prev => [...prev, { sender: 'user', text: userMsg, type: 'normal' }]);
    setInput('');

    setTimeout(() => {
      const lower = userMsg.toLowerCase();
      const match = KNOWLEDGE_BASE.find(k => lower.includes(k.q));
      
      if (match) {
        setMessages(prev => [...prev, { 
          sender: 'assistant', 
          text: match.a, 
          type: match.ai ? 'ai' : 'known',
          conf: match.conf
        }]);
      } else {
        setMessages(prev => [...prev, { 
          sender: 'assistant', 
          text: "I do not have a known procedure for this in my local database. As we are currently offline, I cannot query Earth mainframes. Attempting heuristic analysis... (Analysis inconclusive)", 
          type: 'ai',
          conf: '12%'
        }]);
      }
    }, 600);
  };

  return (
    <div className="flex flex-col h-[calc(100vh-8rem)] w-full max-w-container-max mx-auto px-margin-mobile py-4 gap-unit relative">
      <div className="flex items-center justify-between border-b border-outline-variant pb-2 mb-4">
        <h1 className="font-label-caps text-label-caps text-on-surface-variant uppercase tracking-widest">Offline Assistant - Local Knowledge</h1>
        <div className={clsx("flex items-center gap-2 px-2 py-1 border rounded", state.connectionStatus === 'connected' ? 'border-primary bg-surface-bright' : 'border-outline-variant bg-surface-container-high')}>
          <span className={clsx("w-2 h-2 rounded-full", state.connectionStatus === 'connected' ? 'bg-primary' : 'bg-error')}></span>
          <span className={clsx("font-label-caps text-label-caps uppercase", state.connectionStatus === 'connected' ? 'text-primary' : 'text-error')}>
            {state.connectionStatus === 'connected' ? 'ONLINE' : 'DISCONNECTED'}
          </span>
        </div>
      </div>

      <div className="flex flex-wrap gap-2 mb-4">
        {['Equipment', 'Medical', 'Life Support'].map(t => (
          <button key={t} onClick={() => setInput(t)} className="border border-outline-variant bg-surface-container px-3 py-1.5 font-label-caps text-label-caps hover:bg-surface-bright transition-colors">
            {t}
          </button>
        ))}
      </div>

      <div className="flex-1 overflow-y-auto flex flex-col gap-6 border border-outline-variant bg-surface-container-lowest p-4">
        {messages.map((msg, i) => (
          <div key={i} className={clsx(
            "p-3 relative max-w-[90%]",
            msg.sender === 'user' ? "self-end bg-surface border-outline-variant border" : "self-start bg-surface-container-low border",
            msg.type === 'ai' && msg.sender !== 'user' ? "border-error" : "border-outline-variant"
          )}>
            {msg.sender === 'assistant' && (
              <>
                <div className={clsx("absolute -top-3 left-3 px-2 py-0.5 border flex items-center gap-1", 
                  msg.type === 'ai' ? "bg-error text-on-error border-error" : "bg-primary text-on-primary border-primary"
                )}>
                  <span className="material-symbols-outlined text-[10px]">{msg.type === 'ai' ? 'smart_toy' : 'database'}</span>
                  <span className="font-label-caps text-[10px] leading-none uppercase">{msg.type === 'ai' ? 'AI-Generated' : 'Known Procedure'}</span>
                </div>
                {msg.conf && (
                  <div className="absolute -top-3 right-3 bg-surface text-error px-2 py-0.5 border border-error">
                    <span className="font-label-caps text-[10px] leading-none uppercase">CONFIDENCE: {msg.conf}</span>
                  </div>
                )}
              </>
            )}
            <div className={clsx("font-data-md text-data-md", msg.sender === 'assistant' ? 'mt-2' : '')}>
              {msg.text}
            </div>
          </div>
        ))}
      </div>

      <div className="mt-4 flex gap-2">
        <input 
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && handleSend()}
          className="flex-1 bg-surface border border-outline-variant text-on-surface font-data-md px-3 py-2 focus:outline-none focus:border-primary focus:border-2 transition-all rounded-none placeholder:text-on-surface-variant" 
          placeholder="Query offline database..." 
        />
        <button onClick={handleSend} className="bg-primary text-on-primary px-4 py-2 font-label-caps text-label-caps border border-primary hover:bg-surface-bright hover:text-primary transition-colors flex items-center justify-center">
          <span className="material-symbols-outlined">send</span>
        </button>
      </div>
    </div>
  );
}
