import React from 'react';

interface BottomNavProps {
  currentView: 'home' | 'search' | 'bookmarks' | 'profile' | 'notifications';
  onNavigate: (view: 'home' | 'search' | 'bookmarks' | 'profile' | 'notifications') => void;
  isSearchOverlayOpen?: boolean;
  onCloseSearchOverlay?: () => void;
  isHidden?: boolean;
}

const BottomNav: React.FC<BottomNavProps> = ({ 
    currentView, 
    onNavigate, 
    isSearchOverlayOpen = false, 
    onCloseSearchOverlay,
    isHidden = false
}) => {
  
  const isSearchActive = currentView === 'search' || isSearchOverlayOpen;

  const getIconColor = (view: string) => 
    currentView === view && !isSearchOverlayOpen
      ? "text-black dark:text-white" 
      : "text-zinc-400 dark:text-zinc-600 hover:text-zinc-700 dark:hover:text-zinc-300";

  const getIconClass = (view: string) => 
    currentView === view && !isSearchOverlayOpen ? "material-icons-round" : "material-icons-outlined";

  const handleSearchToggle = () => {
    if (isSearchOverlayOpen && onCloseSearchOverlay) {
        onCloseSearchOverlay();
    } else if (currentView === 'search') {
        onNavigate('home'); 
    } else {
        onNavigate('search');
    }
  };

  return (
    <nav 
        className={`md:hidden absolute bottom-8 left-1/2 transform -translate-x-1/2 bg-white/90 dark:bg-[#1C1C1E]/90 backdrop-blur-2xl shadow-[0_8px_32px_rgba(0,0,0,0.12)] border border-white/20 dark:border-white/10 rounded-full flex items-center z-[101] transition-all duration-500 ease-[cubic-bezier(0.25,1,0.5,1)] ${
            isSearchOverlayOpen 
            ? 'w-[56px] h-[56px] justify-center px-0' 
            : 'w-[280px] h-[60px] justify-between px-1'
        } ${isHidden ? 'translate-y-[200%] opacity-0 pointer-events-none' : 'translate-y-0 opacity-100'}`}
    >
      
      {/* Home */}
      <button 
        onClick={() => onNavigate('home')}
        className={`flex flex-col items-center justify-center rounded-full transition-all duration-300 overflow-hidden ${
            isSearchOverlayOpen ? 'w-0 opacity-0' : 'flex-1 py-2 opacity-100'
        } ${currentView === 'home' && !isSearchOverlayOpen ? '' : 'active:scale-90'}`}
        disabled={isSearchOverlayOpen || isHidden}
      >
        <span className={`${getIconClass('home')} text-[26px] ${getIconColor('home')} transition-colors whitespace-nowrap`}>home</span>
      </button>

      {/* Animated Explore/Search / Close Toggle */}
      <button 
        onClick={handleSearchToggle}
        className={`flex flex-col items-center justify-center rounded-full transition-all duration-300 z-10 ${
            isSearchActive ? '' : 'active:scale-90'
        } ${isSearchOverlayOpen ? 'w-full h-full' : 'flex-1 py-2'}`}
        disabled={isHidden}
      >
        <div className="relative w-7 h-7 flex items-center justify-center">
            {/* Explore Icon (Compass) */}
            <span 
                className={`material-icons-round text-[26px] absolute transition-all duration-500 ease-[cubic-bezier(0.25,1,0.5,1)] ${
                    isSearchOverlayOpen 
                    ? 'opacity-0 rotate-180 scale-50 text-black dark:text-white' 
                    : (currentView === 'search' ? 'text-black dark:text-white rotate-0 opacity-100' : 'text-zinc-400 dark:text-zinc-600 rotate-0 opacity-100')
                }`}
            >
                explore
            </span>
            
            {/* Close (X) */}
            <span 
                className={`material-icons-round text-[26px] absolute transition-all duration-500 ease-[cubic-bezier(0.25,1,0.5,1)] ${
                    isSearchOverlayOpen 
                    ? 'opacity-100 rotate-0 scale-125 text-black dark:text-white' 
                    : 'opacity-0 -rotate-180 scale-50 text-black dark:text-white'
                }`}
            >
                close
            </span>
        </div>
      </button>

      {/* Bookmarks */}
      <button 
        onClick={() => onNavigate('bookmarks')}
        className={`flex flex-col items-center justify-center rounded-full transition-all duration-300 overflow-hidden ${
            isSearchOverlayOpen ? 'w-0 opacity-0' : 'flex-1 py-2 opacity-100'
        } ${currentView === 'bookmarks' && !isSearchOverlayOpen ? '' : 'active:scale-90'}`}
        disabled={isSearchOverlayOpen || isHidden}
      >
        <span className={`${getIconClass('bookmarks')} text-[26px] ${getIconColor('bookmarks')} transition-colors whitespace-nowrap`}>
            {currentView === 'bookmarks' ? 'bookmark' : 'bookmark_border'}
        </span>
      </button>

      {/* User Profile */}
      <button 
        onClick={() => onNavigate('profile')}
        className={`flex flex-col items-center justify-center rounded-full transition-all duration-300 overflow-hidden ${
            isSearchOverlayOpen ? 'w-0 opacity-0' : 'flex-1 py-2 opacity-100'
        } ${currentView === 'profile' && !isSearchOverlayOpen ? '' : 'active:scale-90'}`}
        disabled={isSearchOverlayOpen || isHidden}
      >
        <div className={`w-[26px] h-[26px] rounded-full overflow-hidden ring-offset-2 ring-offset-transparent transition-all whitespace-nowrap ${currentView === 'profile' && !isSearchOverlayOpen ? 'ring-2 ring-black dark:ring-white' : 'bg-zinc-200 dark:bg-zinc-700 ring-1 ring-zinc-100 dark:ring-white/10 opacity-80'}`}>
             <img src="https://i.pravatar.cc/150?u=user" className="w-full h-full object-cover" alt="Profile" />
        </div>
      </button>

      {/* Active Indicator - Black / White */}
      <div className={`absolute bottom-1.5 w-1 h-1 bg-black dark:bg-white rounded-full transition-all duration-300 ease-out ${isSearchOverlayOpen ? 'opacity-0 scale-0' : 'opacity-100 scale-100'}`} 
           style={{ 
               left: currentView === 'home' ? '16%' : 
                     currentView === 'search' ? '39%' : 
                     currentView === 'bookmarks' ? '62%' : 
                     currentView === 'profile' ? '85%' : '16%', // Default/Fallback to Home pos
               transform: 'translateX(-50%)',
               opacity: currentView === 'notifications' ? 0 : 1 // Hide indicator for notification view since it has no icon
           }} 
      />
    </nav>
  );
};

export default BottomNav;