import React, { useState, useEffect } from 'react';

interface TopicCustomizationOverlayProps {
  isOpen: boolean;
  onClose: () => void;
  activeTabs: string[];
  allAvailableTopics: string[];
  onSave: (newTabs: string[]) => void;
}

const TopicCustomizationOverlay: React.FC<TopicCustomizationOverlayProps> = ({ 
    isOpen, 
    onClose, 
    activeTabs, 
    allAvailableTopics,
    onSave 
}) => {
  const [isVisible, setIsVisible] = useState(false);
  const [localTabs, setLocalTabs] = useState<string[]>([]);

  useEffect(() => {
    if (isOpen) {
      setIsVisible(true);
      setLocalTabs([...activeTabs]); // Initialize with current tabs
      document.body.style.overflow = 'hidden';
    } else {
      const timer = setTimeout(() => {
          setIsVisible(false);
      }, 300);
      document.body.style.overflow = 'unset';
      return () => clearTimeout(timer);
    }
  }, [isOpen, activeTabs]);

  const handleToggleTopic = (topic: string) => {
    if (topic === 'Latest') return; // Prevent removing Latest

    setLocalTabs(prev => {
        if (prev.includes(topic)) {
            return prev.filter(t => t !== topic);
        } else {
            return [...prev, topic];
        }
    });
  };

  const handleSave = () => {
    onSave(localTabs);
    onClose();
  };

  const availableToAdd = allAvailableTopics.filter(topic => !localTabs.includes(topic) && topic !== 'Latest');

  if (!isOpen && !isVisible) return null;

  return (
    <div className={`fixed inset-0 z-[150] flex items-center justify-center p-4 sm:p-6 transition-opacity duration-300 ${
        isOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'
    }`}>
      
      {/* Backdrop */}
      <div 
        className="absolute inset-0 bg-black/60 backdrop-blur-sm transition-opacity" 
        onClick={onClose}
      />

      {/* Modal Card */}
      <div className={`
          relative w-full max-w-lg bg-white dark:bg-[#1C1C1E] rounded-[24px] shadow-2xl flex flex-col max-h-[85vh]
          transform transition-all duration-300 cubic-bezier(0.2, 0.8, 0.2, 1)
          ${isOpen ? 'scale-100 translate-y-0 opacity-100' : 'scale-95 translate-y-8 opacity-0'}
      `}>
          
        {/* Header */}
        <div className="flex items-center justify-between px-6 py-5 border-b border-slate-100 dark:border-white/5">
            <h2 className="text-[18px] font-black text-slate-900 dark:text-white tracking-tight">
                Customize Feed
            </h2>
            <button 
                onClick={onClose}
                className="w-8 h-8 rounded-full bg-slate-100 dark:bg-white/10 flex items-center justify-center text-slate-500 hover:bg-slate-200 dark:hover:bg-white/20 transition-colors"
            >
                <span className="material-icons-round text-[18px]">close</span>
            </button>
        </div>

        {/* Content Scroll Area */}
        <div className="flex-1 overflow-y-auto px-6 py-6 custom-scrollbar">
            <div className="mb-6">
                <p className="text-[14px] font-medium text-slate-500 dark:text-slate-400 leading-relaxed">
                    Personalize your navigation bar. Changes are saved automatically to your profile.
                </p>
            </div>

            {/* Active Topics */}
            <div className="mb-8">
                <div className="flex items-center justify-between mb-3">
                     <h3 className="text-[12px] font-bold text-slate-900 dark:text-white uppercase tracking-widest">
                        Selected Topics
                    </h3>
                    <span className="text-[10px] font-bold bg-slate-100 dark:bg-white/10 px-2 py-0.5 rounded text-slate-500 border border-slate-200 dark:border-white/5">
                        {localTabs.length}
                    </span>
                </div>
                
                <div className="flex flex-wrap gap-2">
                    {localTabs.map((topic) => {
                        const isLatest = topic === 'Latest';
                        return (
                            <button
                                key={topic}
                                onClick={() => handleToggleTopic(topic)}
                                disabled={isLatest}
                                className={`group flex items-center gap-2 px-3 py-1.5 rounded-lg transition-all duration-200 active:scale-95 border ${
                                    isLatest
                                    ? 'bg-slate-100 dark:bg-white/5 border-transparent text-slate-400 cursor-default pl-3 pr-3' 
                                    : 'bg-slate-900 dark:bg-white border-transparent text-white dark:text-black shadow-md hover:bg-slate-800 dark:hover:bg-gray-200'
                                }`}
                            >
                                <span className="text-[13px] font-bold">{topic}</span>
                                {isLatest ? (
                                    <span className="material-icons-round text-[14px] text-slate-400">lock</span>
                                ) : (
                                    <span className="material-icons-round text-[16px] opacity-60 group-hover:opacity-100">close</span>
                                )}
                            </button>
                        );
                    })}
                </div>
            </div>

            <div className="h-px bg-slate-100 dark:bg-white/5 w-full mb-8" />

            {/* Available Topics */}
            <div>
                <h3 className="text-[12px] font-bold text-slate-900 dark:text-white uppercase tracking-widest mb-3">
                    Available to Add
                </h3>
                <div className="flex flex-wrap gap-2">
                    {availableToAdd.map((topic) => (
                        <button
                            key={topic}
                            onClick={() => handleToggleTopic(topic)}
                            className="group flex items-center gap-1.5 px-3 py-1.5 rounded-lg border border-slate-200 dark:border-white/10 bg-white dark:bg-white/5 hover:border-primary/50 dark:hover:border-primary/50 text-slate-600 dark:text-slate-300 transition-all duration-200 active:scale-95 hover:bg-slate-50 dark:hover:bg-white/10"
                        >
                            <span className="material-icons-round text-[16px] text-slate-400 group-hover:text-primary transition-colors">add</span>
                            <span className="text-[13px] font-semibold group-hover:text-primary transition-colors">{topic}</span>
                        </button>
                    ))}
                    {availableToAdd.length === 0 && (
                        <div className="w-full py-6 text-center bg-slate-50 dark:bg-white/5 rounded-xl border border-dashed border-slate-200 dark:border-white/10">
                            <span className="text-[13px] text-slate-400 font-medium">All available topics added</span>
                        </div>
                    )}
                </div>
            </div>
        </div>

        {/* Footer */}
        <div className="p-5 border-t border-slate-100 dark:border-white/5 bg-white dark:bg-[#1C1C1E] rounded-b-[24px] flex justify-end gap-3">
            <button 
                onClick={onClose}
                className="px-5 py-2.5 rounded-lg text-[13px] font-bold text-slate-500 hover:text-slate-900 dark:text-slate-400 dark:hover:text-white hover:bg-slate-50 dark:hover:bg-white/5 transition-colors"
            >
                Cancel
            </button>
            <button 
                onClick={handleSave}
                className="px-6 py-2.5 rounded-lg bg-primary text-white text-[13px] font-bold shadow-lg shadow-primary/30 hover:bg-primary-dark transition-all active:scale-95"
            >
                Save Changes
            </button>
        </div>
      </div>
    </div>
  );
};

export default TopicCustomizationOverlay;