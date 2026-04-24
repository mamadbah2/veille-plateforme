import React, { useMemo } from 'react';
import { Tab } from '../types';

interface HeaderProps {
  activeTab: Tab;
  tabs: Tab[];
  onTabChange: (tab: Tab) => void;
  onOpenNotifications: () => void;
  hasUnreadNotifications: boolean;
  onSearchClick: () => void;
  onCustomizeTabs: () => void;
}

const Header: React.FC<HeaderProps> = ({ 
    activeTab, 
    tabs, 
    onTabChange, 
    onOpenNotifications, 
    hasUnreadNotifications, 
    onSearchClick,
    onCustomizeTabs
}) => {
  // Get dynamic date for the header
  const currentDate = useMemo(() => {
    return new Date().toLocaleDateString('en-US', { 
      weekday: 'long', 
      month: 'long', 
      day: 'numeric' 
    }).toUpperCase();
  }, []);

  return (
    <div className="sticky top-0 z-20 transition-colors duration-300">
      {/* Blur Background Layer */}
      <div className="absolute inset-0 bg-white/90 dark:bg-black/90 backdrop-blur-xl border-b border-zinc-200/60 dark:border-white/5"></div>
      
      {/* Content Layer */}
      <div className="relative z-10">
        {/* App Bar */}
        <header className="px-6 pt-6 pb-4 flex justify-between items-end">
            <div className="flex flex-col">
                <span className="text-[11px] font-bold text-zinc-400 uppercase tracking-widest mb-1 animate-[fadeIn_0.5s_ease-out]">{currentDate}</span>
                <h1 className="text-[32px] md:text-[36px] font-black text-black dark:text-white tracking-tighter leading-none">
                Discover
                </h1>
            </div>
            
            <div className="flex items-center gap-3 pb-1">
                {/* Search Bar (Fake Input) - Desktop/Tablet */}
                <button 
                    onClick={onSearchClick}
                    className="hidden md:flex items-center bg-zinc-100 dark:bg-zinc-900 active:scale-95 hover:bg-zinc-200 dark:hover:bg-zinc-800 rounded-full px-4 h-10 w-48 lg:w-64 transition-all duration-200 group text-left"
                    aria-label="Search"
                >
                    <span className="material-icons-outlined text-zinc-400 text-[20px] mr-2 group-hover:text-primary transition-colors">search</span>
                    <span className="text-[13px] text-zinc-500 dark:text-zinc-400 font-semibold group-hover:text-black dark:group-hover:text-zinc-200 transition-colors">Search...</span>
                </button>

                {/* Notifications */}
                <button 
                    onClick={onOpenNotifications}
                    className="w-10 h-10 rounded-full bg-zinc-50 dark:bg-white/5 border border-zinc-100 dark:border-white/10 active:bg-zinc-200 dark:active:bg-white/20 hover:bg-zinc-100 dark:hover:bg-white/10 transition-colors flex items-center justify-center relative"
                    aria-label="Notifications"
                >
                    <span className="material-icons-outlined text-black dark:text-white text-[22px]">notifications_none</span>
                    {hasUnreadNotifications && (
                        <span className="absolute top-2.5 right-3 w-2 h-2 bg-primary rounded-full ring-2 ring-white dark:ring-black"></span>
                    )}
                </button>
            </div>
        </header>

        {/* Chips / Tabs - Scrollable */}
        <div className="flex items-center overflow-x-auto hide-scrollbar px-6 gap-2.5 pb-4">
            {/* Dynamic Tabs */}
            {tabs.map((tab) => {
            const isActive = activeTab === tab;
            return (
                <button
                key={tab}
                onClick={() => onTabChange(tab)}
                className={`px-4 py-2 rounded-full text-[13px] font-bold transition-all duration-200 whitespace-nowrap active:scale-95 ${
                    isActive
                    ? 'bg-black dark:bg-white text-white dark:text-black shadow-lg shadow-black/10 dark:shadow-white/10'
                    : 'bg-zinc-100 dark:bg-white/5 text-zinc-500 dark:text-zinc-400 hover:bg-zinc-200 dark:hover:bg-white/10'
                }`}
                >
                {tab}
                </button>
            );
            })}

            {/* Customize Button */}
            <button 
                onClick={onCustomizeTabs}
                className="w-9 h-9 rounded-full bg-zinc-100 dark:bg-white/5 flex items-center justify-center text-zinc-400 hover:text-black dark:hover:text-white hover:bg-zinc-200 dark:hover:bg-white/10 transition-all active:scale-90 flex-shrink-0 ml-1"
                title="Customize Topics"
            >
                <span className="material-icons-round text-[18px]">tune</span>
            </button>

            {/* Spacer for right padding */}
            <div className="w-4 flex-shrink-0" />
        </div>
      </div>
    </div>
  );
};

export default Header;