import React, { useState, useEffect } from 'react';

export interface NotificationItem {
  id: string;
  type: 'alert' | 'info' | 'success';
  title: string;
  message: string;
  time: string;
  read: boolean;
}

interface NotificationsOverlayProps {
  isOpen: boolean;
  onClose: () => void;
  notifications: NotificationItem[];
  onMarkAllRead: () => void;
  onMarkAsRead: (id: string) => void;
  onAction: (id: string, action: string) => void;
  onViewAll?: () => void;
}

const NotificationsOverlay: React.FC<NotificationsOverlayProps> = ({ 
  isOpen, 
  onClose, 
  notifications, 
  onMarkAllRead,
  onMarkAsRead,
  onAction,
  onViewAll
}) => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    if (isOpen) {
      setIsVisible(true);
    } else {
      const timer = setTimeout(() => setIsVisible(false), 200);
      return () => clearTimeout(timer);
    }
  }, [isOpen]);

  const handleViewAll = () => {
      if (onViewAll) {
          onClose(); // Close the popup
          onViewAll(); // Open the full view
      }
  };

  if (!isOpen && !isVisible) return null;

  return (
    <>
      {/* Invisible backdrop to close on click outside */}
      <div 
        className="fixed inset-0 z-[50] cursor-default" 
        onClick={onClose}
      />

      {/* Popover Card */}
      <div 
        className={`absolute top-[72px] right-4 md:right-6 w-[360px] max-w-[calc(100vw-32px)] bg-white dark:bg-[#1C1C1E] rounded-xl shadow-[0_10px_40px_-10px_rgba(0,0,0,0.15)] ring-1 ring-zinc-900/5 dark:ring-white/10 z-[60] flex flex-col overflow-hidden transition-all duration-200 origin-top-right ${
            isOpen ? 'opacity-100 scale-100 translate-y-0' : 'opacity-0 scale-95 -translate-y-2'
        }`}
      >
        {/* Header */}
        <div className="px-5 py-4 border-b border-zinc-100 dark:border-white/5 flex items-center justify-between bg-white dark:bg-[#1C1C1E]">
            <h2 className="text-[15px] font-bold text-black dark:text-white">Notifications</h2>
            <div className="flex gap-3">
                <button 
                    onClick={onMarkAllRead}
                    className="text-[12px] font-medium text-primary hover:text-primary-dark transition-colors"
                >
                    Mark all read
                </button>
            </div>
        </div>

        {/* List */}
        <div className="max-h-[60vh] overflow-y-auto custom-scrollbar bg-zinc-50 dark:bg-black/20">
            {notifications.length > 0 ? (
                <div className="py-2">
                    {notifications.slice(0, 5).map((notif) => (
                        <div 
                            key={notif.id} 
                            onClick={() => onAction(notif.id, 'view')}
                            className={`px-5 py-3 hover:bg-zinc-100 dark:hover:bg-white/5 transition-colors cursor-pointer border-b border-zinc-100 dark:border-white/5 last:border-0 relative group ${
                                !notif.read ? 'bg-violet-50/50 dark:bg-violet-900/10' : ''
                            }`}
                        >
                            <div className="flex gap-3 items-start">
                                {/* Icon */}
                                <div className={`mt-0.5 w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 ${
                                    notif.type === 'alert' ? 'bg-red-100 text-red-500 dark:bg-red-500/20' : 
                                    notif.type === 'success' ? 'bg-green-100 text-green-500 dark:bg-green-500/20' : 
                                    'bg-violet-100 text-primary dark:bg-violet-500/20'
                                }`}>
                                    <span className="material-icons-round text-[16px]">
                                        {notif.type === 'alert' ? 'priority_high' : notif.type === 'success' ? 'check' : 'notifications'}
                                    </span>
                                </div>

                                <div className="flex-1 min-w-0">
                                    <div className="flex justify-between items-start">
                                        <p className={`text-[13px] font-semibold leading-snug mb-0.5 ${notif.read ? 'text-zinc-700 dark:text-zinc-300' : 'text-black dark:text-white'}`}>
                                            {notif.title}
                                        </p>
                                        {!notif.read && <span className="w-1.5 h-1.5 rounded-full bg-primary mt-1.5 flex-shrink-0"></span>}
                                    </div>
                                    <p className="text-[12px] text-zinc-500 dark:text-zinc-400 leading-normal line-clamp-2">
                                        {notif.message}
                                    </p>
                                    <p className="text-[10px] text-zinc-400 mt-1 font-medium">
                                        {notif.time}
                                    </p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            ) : (
                <div className="py-12 px-6 text-center">
                    <div className="w-12 h-12 bg-zinc-100 dark:bg-white/5 rounded-full flex items-center justify-center mx-auto mb-3">
                         <span className="material-icons-outlined text-zinc-400 text-[20px]">notifications_off</span>
                    </div>
                    <p className="text-[13px] text-zinc-500 font-medium">No new notifications</p>
                </div>
            )}
        </div>

        {/* Footer */}
        <div className="px-5 py-3 bg-white dark:bg-[#1C1C1E] border-t border-zinc-100 dark:border-white/5 text-center">
             <button 
                onClick={handleViewAll}
                className="text-[12px] font-bold text-zinc-500 hover:text-black dark:text-zinc-400 dark:hover:text-white transition-colors"
            >
                 View all notifications
             </button>
        </div>
      </div>
    </>
  );
};

export default NotificationsOverlay;