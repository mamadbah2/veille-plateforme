import React, { useState, useEffect } from 'react';

interface SettingsOverlayProps {
  isOpen: boolean;
  onClose: () => void;
}

const SettingsOverlay: React.FC<SettingsOverlayProps> = ({ isOpen, onClose }) => {
  const [isVisible, setIsVisible] = useState(false);
  const [activeTab, setActiveTab] = useState('Account');

  const TABS = ['Account', 'Publishing', 'Notifications', 'Membership and payment', 'Security and apps'];
  
  // Mock User Data matching App.tsx context
  const user = {
      email: "adegbitewaris@gmail.com",
      username: "@warisadegbite",
      name: "waris adegbite",
      avatarUrl: "https://i.pravatar.cc/150?u=user"
  };

  useEffect(() => {
    if (isOpen) {
      setIsVisible(true);
      document.body.style.overflow = 'hidden';
    } else {
      const timer = setTimeout(() => setIsVisible(false), 300);
      document.body.style.overflow = 'unset';
      return () => clearTimeout(timer);
    }
  }, [isOpen]);

  if (!isOpen && !isVisible) return null;

  return (
    <div 
      className={`fixed inset-0 md:left-[88px] z-[150] bg-white dark:bg-black overflow-y-auto transition-opacity duration-300 ${
        isOpen ? 'opacity-100' : 'opacity-0'
      }`}
    >
        {/* Top Navigation / Close Bar */}
        <div className="sticky top-0 z-20 bg-white/95 dark:bg-black/95 backdrop-blur-md border-b border-transparent px-6 py-4 flex items-center justify-between max-w-[1080px] mx-auto w-full">
            <div className="flex items-center gap-4">
                <button 
                    onClick={onClose}
                    className="w-8 h-8 flex items-center justify-center text-slate-400 hover:text-slate-900 dark:hover:text-white transition-colors"
                >
                    <span className="material-icons-round">arrow_back</span>
                </button>
                <span className="text-[14px] font-medium text-slate-500 md:hidden">Settings</span>
            </div>
        </div>

        <div className="max-w-[1080px] mx-auto w-full px-6 md:px-12 pb-24 pt-4">
            
            <div className="flex flex-col md:flex-row gap-16 lg:gap-24 relative">
                
                {/* LEFT COLUMN: Main Settings */}
                <div className="flex-1 min-w-0">
                    <h1 className="text-[42px] font-bold text-slate-900 dark:text-white tracking-tight mb-10 font-display">
                        Settings
                    </h1>

                    {/* Tabs */}
                    <div className="flex items-center gap-6 overflow-x-auto hide-scrollbar border-b border-slate-200 dark:border-white/10 mb-10">
                        {TABS.map(tab => (
                            <button
                                key={tab}
                                onClick={() => setActiveTab(tab)}
                                className={`pb-3 text-[14px] whitespace-nowrap transition-colors relative ${
                                    activeTab === tab 
                                    ? 'text-slate-900 dark:text-white font-medium' 
                                    : 'text-slate-500 hover:text-slate-900 dark:text-slate-400 dark:hover:text-white'
                                }`}
                            >
                                {tab}
                                {activeTab === tab && (
                                    <div className="absolute bottom-0 left-0 right-0 h-[1px] bg-slate-900 dark:bg-white" />
                                )}
                            </button>
                        ))}
                    </div>

                    {/* Content Area - Account Tab */}
                    {activeTab === 'Account' && (
                        <div className="space-y-0">
                            
                            {/* Email */}
                            <SettingRow label="Email address">
                                <span className="text-[14px] text-slate-500 dark:text-slate-400">{user.email}</span>
                            </SettingRow>

                            {/* Username */}
                            <SettingRow label="Username and subdomain">
                                <span className="text-[14px] text-slate-500 dark:text-slate-400">{user.username}</span>
                            </SettingRow>

                            {/* Profile Info */}
                            <SettingRow 
                                label="Profile information" 
                                description="Edit your photo, name, pronouns, short bio, etc."
                            >
                                <div className="flex items-center gap-3 cursor-pointer group">
                                    <span className="text-[14px] text-slate-500 group-hover:text-slate-900 dark:text-slate-400 dark:group-hover:text-white transition-colors">
                                        {user.name}
                                    </span>
                                    <div className="w-8 h-8 rounded-full overflow-hidden bg-slate-100">
                                        <img src={user.avatarUrl} alt="" className="w-full h-full object-cover" />
                                    </div>
                                </div>
                            </SettingRow>

                            {/* Profile Design */}
                            <SettingRow 
                                label="Profile design" 
                                description="Customize the appearance of your profile."
                                isLink
                            />

                            {/* Custom Domain */}
                            <SettingRow 
                                label="Custom domain" 
                                description="Upgrade to a Membership to redirect your profile URL to a domain like yourdomain.com."
                                isLink
                            >
                                <div className="flex items-center gap-2">
                                    <span className="text-[14px] text-slate-500 dark:text-slate-400">None</span>
                                    <span className="material-icons-round text-[18px] text-slate-400">north_east</span>
                                </div>
                            </SettingRow>

                            {/* Partner Program */}
                            <SettingRow 
                                label="Partner Program" 
                                description="You are not enrolled in the Partner Program."
                                isLink
                            />

                            {/* Digest Frequency */}
                            <SettingRow 
                                label="Your Daily Digest frequency" 
                                description="Adjust how often you see a new Digest."
                            >
                                <div className="flex items-center gap-1 text-[#1A8917] dark:text-green-400 cursor-pointer">
                                    <span className="text-[14px]">Daily</span>
                                    <span className="material-icons-round text-[18px]">keyboard_arrow_down</span>
                                </div>
                            </SettingRow>

                            {/* Feedback */}
                            <SettingRow 
                                label="Provide Feedback" 
                                description="Receive occasional invitations to share your feedback."
                                border={false}
                            >
                                <div className="w-5 h-5 border border-slate-300 dark:border-white/20 rounded cursor-pointer hover:border-slate-500"></div>
                            </SettingRow>

                             {/* Bottom Section: Recommendations (Extra from screenshot bottom) */}
                            <div className="pt-10 mt-2">
                                <SettingRow 
                                    label="Refine recommendations" 
                                    description="Adjust recommendations by removing stories you've read."
                                    isLink
                                    border={false}
                                />
                                <SettingRow 
                                    label="Muted writers and publications" 
                                    isLink
                                    border={false}
                                />
                            </div>

                        </div>
                    )}
                </div>

                {/* RIGHT COLUMN: Sidebar (Sticky) */}
                <div className="w-full md:w-[280px] lg:w-[300px] flex-shrink-0 hidden md:block">
                    <div className="sticky top-12 pt-10">
                        <h3 className="text-[14px] font-bold text-slate-900 dark:text-white mb-4">
                            Suggested help articles
                        </h3>
                        <ul className="space-y-4">
                            {[
                                'Sign in or sign up to DevPulse',
                                'Your profile page',
                                'Writing and publishing your first story',
                                'About distribution system',
                                'Get started with the Partner Program'
                            ].map((link, i) => (
                                <li key={i}>
                                    <a href="#" className="text-[13px] text-slate-500 hover:text-slate-900 dark:text-slate-400 dark:hover:text-white transition-colors">
                                        {link}
                                    </a>
                                </li>
                            ))}
                        </ul>
                    </div>
                </div>

            </div>
        </div>
    </div>
  );
};

// --- Helper Component for Rows ---
interface SettingRowProps {
    label: string;
    description?: string;
    children?: React.ReactNode;
    isLink?: boolean;
    border?: boolean;
}

const SettingRow: React.FC<SettingRowProps> = ({ label, description, children, isLink, border = true }) => {
    return (
        <div className={`py-6 flex justify-between items-start gap-4 ${border ? 'border-b border-slate-100 dark:border-white/5' : ''}`}>
            <div className="max-w-lg">
                <h3 className="text-[15px] font-medium text-slate-900 dark:text-white mb-1">
                    {label}
                </h3>
                {description && (
                    <p className="text-[13px] text-slate-500 dark:text-slate-400 leading-normal max-w-sm">
                        {description}
                    </p>
                )}
                {/* Link underline provided by context usually, mimicking structure */}
                {description && description.includes("Share feedback") && (
                     <button className="text-[13px] text-slate-500 underline mt-1">Share feedback now</button>
                )}
            </div>
            
            <div className="flex-shrink-0 flex items-center">
                {children}
                {isLink && !children && (
                    <span className="material-icons-round text-[18px] text-slate-400">north_east</span>
                )}
            </div>
        </div>
    );
};

export default SettingsOverlay;