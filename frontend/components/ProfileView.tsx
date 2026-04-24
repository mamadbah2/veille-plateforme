import React, { useState } from 'react';
import { Article, TextSize } from '../types';

interface ProfileViewProps {
    isLoading?: boolean;
    onOpenSettings?: () => void;
    savedCount?: number;
    lastSavedArticle?: Article | null;
    onNavigateToLibrary?: () => void;
    isDarkMode?: boolean;
    onToggleDarkMode?: () => void;
    textSize?: TextSize;
    onToggleTextSize?: () => void;
}

const ProfileView: React.FC<ProfileViewProps> = ({ 
    isLoading = false, 
    onOpenSettings,
    savedCount = 0,
    lastSavedArticle,
    onNavigateToLibrary,
    isDarkMode,
    onToggleDarkMode,
    textSize,
    onToggleTextSize
}) => {
  const [activeTab, setActiveTab] = useState<'Home' | 'About'>('Home');

  if (isLoading) {
      return <div className="p-10 text-center text-zinc-400">Loading profile...</div>;
  }

  // Mock User Data (In a real app, this would come from a user context or prop)
  const user = {
      name: "Waris Adegbite",
      avatarUrl: "https://i.pravatar.cc/150?u=user",
      username: "@warisadegbite"
  };

  return (
    <div className="flex flex-col h-full bg-white dark:bg-black font-sans overflow-y-auto hide-scrollbar animate-fadeIn">
      
      {/* Main Container */}
      <div className="max-w-[1000px] mx-auto w-full pt-12 px-6 pb-20">
        
        <div className="flex flex-col-reverse md:flex-row gap-12 lg:gap-24 relative">
            
            {/* LEFT COLUMN: Main Content */}
            <div className="flex-1 min-w-0">
                
                {/* Mobile Header (Visible only on small screens) */}
                <div className="md:hidden flex items-center justify-between mb-8">
                    <h1 className="text-[32px] font-bold text-black dark:text-white tracking-tight">
                        {user.name}
                    </h1>
                    <button className="text-zinc-400 hover:text-zinc-600 dark:hover:text-zinc-200">
                        <span className="material-icons-round text-[24px]">more_horiz</span>
                    </button>
                </div>

                {/* Main Desktop Header (Hidden on mobile to match layout flow) */}
                <div className="hidden md:flex items-center justify-between mb-10">
                    <h1 className="text-[42px] font-bold text-black dark:text-white tracking-tight">
                        {user.name}
                    </h1>
                    <button className="text-zinc-400 hover:text-zinc-600 dark:hover:text-zinc-200">
                        <span className="material-icons-round text-[24px]">more_horiz</span>
                    </button>
                </div>

                {/* Tabs */}
                <div className="flex items-center border-b border-zinc-100 dark:border-white/10 mb-8">
                    <button 
                        onClick={() => setActiveTab('Home')}
                        className={`mr-8 pb-3 text-[14px] transition-colors relative ${
                            activeTab === 'Home' 
                            ? 'text-black dark:text-white font-medium' 
                            : 'text-zinc-500 dark:text-zinc-400 hover:text-zinc-700'
                        }`}
                    >
                        Home
                        {activeTab === 'Home' && <div className="absolute bottom-0 left-0 right-0 h-[1px] bg-black dark:bg-white"></div>}
                    </button>
                    <button 
                        onClick={() => setActiveTab('About')}
                        className={`mr-8 pb-3 text-[14px] transition-colors relative ${
                            activeTab === 'About' 
                            ? 'text-black dark:text-white font-medium' 
                            : 'text-zinc-500 dark:text-zinc-400 hover:text-zinc-700'
                        }`}
                    >
                        About
                        {activeTab === 'About' && <div className="absolute bottom-0 left-0 right-0 h-[1px] bg-black dark:bg-white"></div>}
                    </button>
                </div>

                {/* Tab Content */}
                <div>
                    {activeTab === 'Home' ? (
                        <div className="space-y-8">
                            {/* Reading List Card - Linked to Library */}
                            <div 
                                onClick={onNavigateToLibrary}
                                className="group border border-zinc-100 dark:border-white/10 bg-zinc-50/50 dark:bg-white/5 rounded-lg flex h-[180px] md:h-[160px] overflow-hidden cursor-pointer hover:border-zinc-300 dark:hover:border-white/20 transition-colors"
                            >
                                
                                {/* Card Content */}
                                <div className="flex-1 p-6 flex flex-col justify-between min-w-0">
                                    <div className="flex items-center gap-2 mb-2">
                                        <div className="w-5 h-5 rounded-full overflow-hidden">
                                            <img src={user.avatarUrl} alt={user.name} className="w-full h-full object-cover" />
                                        </div>
                                        <span className="text-[13px] font-medium text-zinc-700 dark:text-zinc-200 truncate">{user.name}</span>
                                    </div>

                                    <h3 className="text-[18px] md:text-[22px] font-bold text-black dark:text-white leading-tight mb-auto">
                                        Reading list
                                    </h3>

                                    <div className="flex items-center justify-between mt-4">
                                        <div className="flex items-center gap-2 text-[13px] text-zinc-500 dark:text-zinc-400">
                                            <span>{savedCount === 0 ? 'No stories' : `${savedCount} stories`}</span>
                                            <span className="material-icons-round text-[14px] opacity-70">lock</span>
                                        </div>
                                        <span className="material-icons-round text-[20px] text-zinc-400 group-hover:text-black dark:group-hover:text-white transition-colors">arrow_forward</span>
                                    </div>
                                </div>

                                {/* Stack Visuals */}
                                <div className="flex bg-white dark:bg-black/20 border-l border-zinc-100 dark:border-white/5">
                                     {/* First Bar (Show image if available) */}
                                     <div className="w-[80px] md:w-[120px] h-full bg-zinc-100 dark:bg-white/5 border-r border-white dark:border-black overflow-hidden relative">
                                        {lastSavedArticle && (
                                            <img src={lastSavedArticle.thumbnailUrl} className="w-full h-full object-cover opacity-80 group-hover:opacity-100 transition-opacity" alt="" />
                                        )}
                                     </div>
                                     <div className="w-[40px] md:w-[60px] h-full bg-zinc-100 dark:bg-white/5 border-r border-white dark:border-black hidden sm:block"></div>
                                     <div className="w-[20px] md:w-[30px] h-full bg-zinc-100 dark:bg-white/5 hidden sm:block"></div>
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className="py-10 text-center border-t border-zinc-100 dark:border-white/10 mt-8">
                             <p className="text-zinc-500 dark:text-zinc-400 italic">Tell the world about yourself.</p>
                             <button className="mt-4 text-primary font-medium text-[14px] hover:underline">
                                 Get started
                             </button>
                        </div>
                    )}
                </div>

            </div>

            {/* RIGHT COLUMN: Sidebar (Fixed/Sticky behavior) */}
            <div className="w-full md:w-[280px] lg:w-[320px] flex-shrink-0">
                <div className="sticky top-12">
                    
                    {/* Big Avatar - Adapted to use App's Photo style but size of screenshot */}
                    <div className="w-24 h-24 lg:w-32 lg:h-32 rounded-full overflow-hidden bg-zinc-100 dark:bg-white/10 mb-5 ring-4 ring-white dark:ring-white/5 shadow-sm">
                        <img src={user.avatarUrl} alt={user.name} className="w-full h-full object-cover" />
                    </div>

                    {/* Name */}
                    <h2 className="text-[18px] font-bold text-black dark:text-white mb-1">
                        {user.name}
                    </h2>
                    <p className="text-[14px] text-zinc-500 dark:text-zinc-400 mb-4">
                        {user.username}
                    </p>

                    {/* Edit Profile - Adapted to App Color (Primary Blue) */}
                    <button className="text-[14px] text-primary hover:text-primary-dark font-medium mb-8">
                        Edit profile
                    </button>
                    
                    {/* App Settings Link with Toggles */}
                    <div className="pt-6 border-t border-zinc-100 dark:border-white/10 space-y-3">
                        {onToggleDarkMode && (
                            <button 
                                onClick={onToggleDarkMode}
                                className="w-full text-left flex items-center gap-3 text-[13px] text-zinc-500 hover:text-black dark:hover:text-white transition-colors"
                            >
                                <span className="material-icons-outlined text-[16px]">
                                    {isDarkMode ? 'light_mode' : 'dark_mode'}
                                </span>
                                {isDarkMode ? 'Light Mode' : 'Dark Mode'}
                            </button>
                        )}
                        
                        {onToggleTextSize && (
                            <button 
                                onClick={onToggleTextSize}
                                className="w-full text-left flex items-center gap-3 text-[13px] text-zinc-500 hover:text-black dark:hover:text-white transition-colors"
                            >
                                <span className="material-icons-outlined text-[16px]">text_fields</span>
                                Text Size: {textSize === 'normal' ? 'Regular' : textSize === 'large' ? 'Large' : 'Extra'}
                            </button>
                        )}

                        <button 
                            onClick={onOpenSettings}
                            className="w-full text-left flex items-center gap-3 text-[13px] text-zinc-500 hover:text-black dark:hover:text-white transition-colors"
                        >
                            <span className="material-icons-outlined text-[16px]">settings</span>
                            App Settings
                        </button>
                    </div>

                </div>
            </div>

        </div>
      </div>
    </div>
  );
};

export default ProfileView;