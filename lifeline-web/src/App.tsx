import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useLocation, Navigate } from 'react-router-dom';
import { useAppContext } from './store/AppContext';
import clsx from 'clsx';

// Placeholder Pages
import Dashboard from './pages/Dashboard';
import Procedures from './pages/Procedures';
import Crew from './pages/Crew';
import Assistant from './pages/Assistant';
import Log from './pages/Log';
import Map from './pages/Map';

function TopBar({ toggleDarkMode }: { toggleDarkMode: () => void }) {
  const { state, dispatch } = useAppContext();
  
  const cycleConnection = () => {
    const next: Record<string, any> = {
      connected: 'delayed',
      delayed: 'offline',
      offline: 'connected'
    };
    dispatch({ type: 'SET_CONNECTION', payload: next[state.connectionStatus] });
  };

  return (
    <header className="fixed top-0 left-0 w-full z-50 flex items-center justify-between px-margin-mobile bg-surface border-b border-outline-variant h-12">
      <div className="flex items-center gap-2 cursor-pointer" onClick={cycleConnection}>
        <span className={clsx("material-symbols-outlined", 
          state.connectionStatus === 'offline' ? 'text-error' : 
          state.connectionStatus === 'delayed' ? 'text-secondary' : 'text-primary'
        )}>
          {state.connectionStatus === 'offline' ? 'signal_cellular_off' : 'signal_cellular_alt'}
        </span>
        <span className={clsx("font-label-caps text-label-caps uppercase",
          state.connectionStatus === 'offline' ? 'text-error' : 
          state.connectionStatus === 'delayed' ? 'text-secondary' : 'text-primary'
        )}>
          {state.connectionStatus}
        </span>
      </div>
      <div className="font-display-lg text-display-lg-mobile tracking-tighter text-primary">LIFELINE</div>
      <button onClick={toggleDarkMode} className="hover:bg-surface-bright active:bg-primary active:text-on-primary transition-colors duration-100 p-1 rounded">
        <span className="material-symbols-outlined text-primary">dark_mode</span>
      </button>
    </header>
  );
}

function NavLinks() {
  const location = useLocation();
  const links = [
    { path: '/dashboard', icon: 'dashboard', label: 'Dash' },
    { path: '/procedures', icon: 'description', label: 'Procs' },
    { path: '/crew', icon: 'group', label: 'Crew' },
    { path: '/map', icon: 'explore', label: 'Map' },
    { path: '/assistant', icon: 'smart_toy', label: 'AI' },
    { path: '/log', icon: 'receipt_long', label: 'Log' },
  ];

  return (
    <>
      {links.map(link => {
        const active = location.pathname.startsWith(link.path);
        return (
          <Link key={link.path} to={link.path} className={clsx(
            "flex flex-col items-center justify-center p-2 transition-transform duration-75 w-full h-full md:w-12 md:h-12 border-r md:border-r-0 md:border-b border-outline-variant last:border-0",
            active ? "bg-primary text-on-primary" : "text-on-surface-variant hover:bg-surface-container-high"
          )}>
            <span className="material-symbols-outlined">{link.icon}</span>
            <span className="font-label-caps text-[10px] md:hidden">{link.label}</span>
          </Link>
        )
      })}
    </>
  );
}

export default function App() {
  const [darkMode, setDarkMode] = useState(true);

  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [darkMode]);

  return (
    <Router>
      <div className="bg-surface-container-lowest text-on-surface font-body-base min-h-screen flex flex-col pt-12 pb-16 md:pb-0">
        <TopBar toggleDarkMode={() => setDarkMode(!darkMode)} />
        
        {/* Desktop Side Nav */}
        <nav className="hidden md:flex fixed top-12 left-0 h-[calc(100vh-3rem)] w-16 bg-surface-container-lowest border-r border-outline-variant flex-col items-center py-4 z-40">
          <NavLinks />
        </nav>

        <main className="flex-grow flex flex-col w-full mx-auto md:ml-16 md:w-[calc(100%-4rem)] max-w-container-max">
          <Routes>
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/procedures" element={<Procedures />} />
            <Route path="/crew" element={<Crew />} />
            <Route path="/map" element={<Map />} />
            <Route path="/assistant" element={<Assistant />} />
            <Route path="/log" element={<Log />} />
          </Routes>
        </main>

        {/* Mobile Bottom Nav */}
        <nav className="fixed bottom-0 left-0 w-full z-50 flex justify-around items-center h-16 bg-surface-container-lowest border-t border-outline-variant md:hidden">
          <NavLinks />
        </nav>
      </div>
    </Router>
  );
}
