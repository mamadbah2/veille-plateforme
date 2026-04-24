import React, { useState, useMemo } from 'react';
import { NotificationItem } from './NotificationsOverlay';

interface NotificationsFullViewProps {
  notifications: NotificationItem[];
  onMarkAsRead: (id: string) => void;
  onDelete: (id: string) => void;
  onMarkAllRead: () => void;
}

const NotificationsFullView: React.FC<NotificationsFullViewProps> = ({
  notifications,
  onMarkAsRead,
  onDelete,
  onMarkAllRead
}) => {
  const [activeTab, setActiveTab] = useState<'All' | 'Unread'>('All');

  // Filtering
  const filteredNotifications = useMemo(() => {
    if (activeTab === 'Unread') {
      return notifications.filter(n => !n.read);
    }
    return notifications;
  }, [notifications, activeTab]);

  return (
    <div className="flex flex-col h-full bg-white dark:bg-black font-sans animate-fadeIn">
      <div className="flex-1 overflow-y-auto hide-scrollbar">
        <div className="max-w-3xl mx-auto px-6 pt-12 pb-20">
            
            {/* Title */}
            <div className="flex items-center justify-between mb-10">
                <h1 className="text-[42px] font-black text-black dark:text-white tracking-tighter leading-none">
                    Notifications
                </h1>
                <button 
                    onClick={onMarkAllRead}
                    className="text-[13px] font-medium text-green-600 hover:text-green-700 dark:text-green-400 hover:underline"
                >
                    Mark all as read
                </button>
            </div>

            {/* Tabs (Text based like screenshot) */}
            <div className="flex items-center border-b border-zinc-200 dark:border-white/10 mb-8">
                {['All', 'Unread'].map((tab) => (
                    <button
                        key={tab}
                        onClick={() => setActiveTab(tab as any)}
                        className={`mr-8 pb-3 text-[14px] font-medium transition-colors relative ${
                            activeTab === tab 
                            ? 'text-black dark:text-white' 
                            : 'text-zinc-500 dark:text-zinc-400 hover:text-zinc-700 dark:hover:text-zinc-300'
                        }`}
                    >
                        {tab}
                        {activeTab === tab && (
                            <div className="absolute bottom-0 left-0 right-0 h-[1px] bg-black dark:bg-white"></div>
                        )}
                    </button>
                ))}
            </div>

            {/* Content */}
            <div>
                {filteredNotifications.length === 0 ? (
                    <div className="py-20 text-center">
                        <p className="text-[15px] text-zinc-500 dark:text-zinc-400">You're all caught up.</p>
                    </div>
                ) : (
                    <div className="space-y-0">
                        {filteredNotifications.map(item => (
                             <div key={item.id} className="flex gap-4 group py-6 border-b border-zinc-100 dark:border-white/5 last:border-0">
                                 {/* Icon / Avatar placeholder */}
                                 <div className={`mt-0.5 w-10 h-10 rounded-full flex items-center justify-center flex-shrink-0 border border-zinc-100 dark:border-white/10 ${
                                     item.type === 'alert' ? 'bg-red-50 text-red-500 dark:bg-red-900/20' : 
                                     item.type === 'success' ? 'bg-green-50 text-green-600 dark:bg-green-900/20' : 
                                     'bg-zinc-50 text-zinc-600 dark:bg-white/5 dark:text-zinc-300'
                                 }`}>
                                     <span className="material-icons-round text-[20px]">
                                         {item.type === 'alert' ? 'priority_high' : item.type === 'success' ? 'check' : 'notifications'}
                                     </span>
                                 </div>

                                 <div className="flex-1 min-w-0">
                                     <div className="flex justify-between items-start mb-1">
                                         <div className="flex items-center gap-2">
                                             <span className="text-[14px] font-bold text-black dark:text-white">
                                                 {item.title} 
                                             </span>
                                             <span className="text-[14px] text-zinc-500 font-normal">· {item.time}</span>
                                             {!item.read && (
                                                <span className="w-2 h-2 rounded-full bg-primary ml-1"></span>
                                             )}
                                         </div>
                                         <button 
                                            onClick={() => onDelete(item.id)}
                                            className="opacity-0 group-hover:opacity-100 text-zinc-400 hover:text-red-500 transition-all p-1"
                                            title="Delete"
                                         >
                                             <span className="material-icons-outlined text-[18px]">close</span>
                                         </button>
                                     </div>
                                     <p className="text-[14px] text-zinc-500 dark:text-zinc-400 leading-relaxed max-w-xl">
                                         {item.message}
                                     </p>
                                     {!item.read && (
                                        <button 
                                            onClick={() => onMarkAsRead(item.id)}
                                            className="mt-2 text-[12px] font-semibold text-primary hover:underline"
                                        >
                                            Mark as read
                                        </button>
                                     )}
                                 </div>
                             </div>
                        ))}
                    </div>
                )}
            </div>

        </div>
      </div>
    </div>
  );
};

export default NotificationsFullView;