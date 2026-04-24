import React, { useState, useRef, useEffect } from 'react';

interface SidebarProps {
  currentView: 'home' | 'search' | 'bookmarks' | 'profile' | 'notifications';
  onNavigate: (view: 'home' | 'search' | 'bookmarks' | 'profile' | 'notifications') => void;
  onOpenSettings: () => void;
}

const Sidebar: React.FC<SidebarProps> = ({ currentView, onNavigate, onOpenSettings }) => {
  const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
  const menuRef = useRef<HTMLDivElement>(null);

  const navItems = [
    { id: 'home', icon: 'home', label: 'Home' },
    { id: 'search', icon: 'explore', label: 'Explorer' },
    { id: 'bookmarks', icon: 'bookmark_border', label: 'Library' },
  ];

  // Close menu on click outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(event.target as Node)) {
        setIsProfileMenuOpen(false);
      }
    };
    if (isProfileMenuOpen) {
      document.addEventListener('mousedown', handleClickOutside);
    }
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [isProfileMenuOpen]);

  const handleProfileClick = (e: React.MouseEvent) => {
      e.stopPropagation();
      setIsProfileMenuOpen(!isProfileMenuOpen);
  };

  const handleMenuAction = (action: () => void) => {
      action();
      setIsProfileMenuOpen(false);
  };

  return (
    <aside className="hidden md:flex flex-col w-[88px] h-screen sticky top-0 border-r border-slate-200 dark:border-white/10 bg-white dark:bg-black z-[160] items-center py-8">
      {/* Brand Icon */}
      <div className="mb-12">
           <div className="w-10 h-10 bg-black dark:bg-white rounded-full flex items-center justify-center cursor-pointer hover:scale-105 transition-transform shadow-lg shadow-black/10 dark:shadow-white/10">
                <span className="material-icons-round text-white dark:text-black text-[24px]">code</span>
           </div>
      </div>

      {/* Main Navigation */}
      <nav className="flex-1 flex flex-col gap-10 w-full items-center">
        {navItems.map((item) => {
            // Explicitly cast item.id to check active status properly against the broader union type
            const isActive = currentView === item.id;
            let displayIcon = item.icon;
            if (isActive) {
                if (item.icon === 'bookmark_border') displayIcon = 'bookmark';
                if (item.icon === 'person_outline') displayIcon = 'person';
            }

            return (
                <button
                    key={item.id}
                    onClick={() => onNavigate(item.id as any)}
                    className="group relative w-full flex justify-center outline-none"
                    aria-label={item.label}
                >
                    <span className={`material-icons-outlined text-[28px] transition-colors duration-200 ${
                        isActive 
                        ? 'text-black dark:text-white' 
                        : 'text-slate-400 hover:text-black dark:hover:text-white'
                    }`}>
                        {displayIcon}
                    </span>
                    
                    {/* Tooltip */}
                    <span className="absolute left-16 top-1/2 -translate-y-1/2 bg-black text-white text-[11px] font-bold px-3 py-1.5 rounded opacity-0 group-hover:opacity-100 pointer-events-none transition-all duration-200 translate-x-2 group-hover:translate-x-0 whitespace-nowrap z-50 shadow-md">
                        {item.label}
                    </span>
                </button>
            );
        })}
        
        <div className="w-8 h-px bg-slate-100 dark:bg-white/10 my-2"></div>

        <button 
            className="group relative w-full flex justify-center text-slate-300 hover:text-black dark:hover:text-white transition-colors outline-none"
            title="Write"
        >
             <span className="material-icons-outlined text-[28px]">edit_note</span>
             <span className="absolute left-16 top-1/2 -translate-y-1/2 bg-black text-white text-[11px] font-bold px-3 py-1.5 rounded opacity-0 group-hover:opacity-100 pointer-events-none transition-all duration-200 translate-x-2 group-hover:translate-x-0 whitespace-nowrap z-50 shadow-md">
                Write
            </span>
        </button>
      </nav>

      {/* User Avatar & Settings + MENU POPOVER */}
      <div className="mt-auto flex flex-col items-center gap-8 relative" ref={menuRef}>
        
        {/* Profile Menu Dropdown */}
        {isProfileMenuOpen && (
            <div className="absolute bottom-0 left-full ml-3 w-[260px] bg-white dark:bg-[#1C1C1E] rounded-lg shadow-2xl border border-slate-100 dark:border-white/10 flex flex-col overflow-hidden animate-[fadeIn_0.2s_ease-out] z-50">
                {/* User Header */}
                <div className="p-4 border-b border-slate-100 dark:border-white/5">
                    <div className="flex items-center gap-3 mb-1">
                        <div className="w-10 h-10 rounded-full overflow-hidden bg-slate-100">
                             <img src="https://i.pravatar.cc/150?u=user" alt="User" className="w-full h-full object-cover" />
                        </div>
                        <div className="flex-1 min-w-0">
                            <h4 className="text-[14px] font-bold text-slate-900 dark:text-white truncate">waris adegbite</h4>
                            <button 
                                onClick={() => handleMenuAction(() => onNavigate('profile'))}
                                className="text-[12px] text-slate-500 hover:text-slate-900 dark:text-slate-400 dark:hover:text-white transition-colors"
                            >
                                View profile
                            </button>
                        </div>
                    </div>
                </div>

                {/* Main Links */}
                <div className="py-2 border-b border-slate-100 dark:border-white/5">
                    <button 
                        onClick={() => handleMenuAction(onOpenSettings)}
                        className="w-full text-left px-5 py-2.5 flex items-center gap-3 hover:bg-slate-50 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors"
                    >
                        <span className="material-icons-outlined text-[20px] text-slate-400">settings</span>
                        <span className="text-[14px] font-medium">Settings</span>
                    </button>
                    <button className="w-full text-left px-5 py-2.5 flex items-center gap-3 hover:bg-slate-50 dark:hover:bg-white/5 text-slate-600 dark:text-slate-300 transition-colors">
                        <span className="material-icons-outlined text-[20px] text-slate-400">help_outline</span>
                        <span className="text-[14px] font-medium">Help</span>
                    </button>
                </div>

                {/* Promo Section */}
                <div className="py-2 border-b border-slate-100 dark:border-white/5">
                    <div className="px-5 py-2">
                        <div className="flex items-center gap-1.5 mb-1">
                            <span className="text-[14px] font-medium text-slate-900 dark:text-white">Become a Medium member</span>
                            <span className="text-[14px]">✨</span>
                        </div>
                        <p className="text-[12px] text-slate-500">Apply to the Partner Program</p>
                    </div>
                </div>

                {/* Sign Out */}
                <div className="py-2 border-b border-slate-100 dark:border-white/5">
                    <div className="px-5 py-2">
                         <button className="text-[14px] font-medium text-slate-600 dark:text-slate-300 hover:text-red-500 dark:hover:text-red-400 transition-colors mb-1">
                             Sign out
                         </button>
                         <p className="text-[11px] text-slate-400 truncate">ad..........@gmail.com</p>
                    </div>
                </div>

                {/* Footer Links */}
                <div className="px-5 py-3 bg-slate-50 dark:bg-black/20">
                     <div className="flex flex-wrap gap-x-3 gap-y-1 text-[11px] text-slate-400">
                         <a href="#" className="hover:underline">About</a>
                         <a href="#" className="hover:underline">Blog</a>
                         <a href="#" className="hover:underline">Careers</a>
                         <a href="#" className="hover:underline">Privacy</a>
                         <a href="#" className="hover:underline">Terms</a>
                         <a href="#" className="hover:underline">Text to speech</a>
                         <a href="#" className="hover:underline">More</a>
                     </div>
                </div>

            </div>
        )}
        
        <button 
            onClick={handleProfileClick} 
            className={`w-9 h-9 rounded-full overflow-hidden transition-all duration-200 outline-none ${
                isProfileMenuOpen || currentView === 'profile' 
                ? 'ring-2 ring-black dark:ring-white ring-offset-2 ring-offset-white dark:ring-offset-black' 
                : 'hover:ring-2 hover:ring-slate-200 dark:hover:ring-white/20'
            }`}
        >
            <img src="https://i.pravatar.cc/150?u=user" alt="User" className="w-full h-full object-cover" />
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;