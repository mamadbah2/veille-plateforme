import React, { useEffect, useState } from 'react';

interface ToastProps {
  message: string;
  isVisible: boolean;
  onHide: () => void;
  icon?: string;
}

const Toast: React.FC<ToastProps> = ({ message, isVisible, onHide, icon = 'check_circle' }) => {
  const [show, setShow] = useState(false);

  useEffect(() => {
    if (isVisible) {
      setShow(true);
      const timer = setTimeout(() => {
        setShow(false);
        setTimeout(onHide, 300); // Wait for exit animation
      }, 2000);
      return () => clearTimeout(timer);
    }
  }, [isVisible, onHide]);

  if (!isVisible && !show) return null;

  return (
    <div className={`fixed top-12 left-1/2 transform -translate-x-1/2 z-[100] transition-all duration-300 ${
        show ? 'translate-y-0 opacity-100' : '-translate-y-4 opacity-0'
    }`}>
      <div className="bg-slate-900/90 dark:bg-white/90 backdrop-blur-md text-white dark:text-slate-900 px-6 py-3 rounded-full shadow-xl flex items-center gap-3 min-w-[200px] justify-center">
        <span className="material-icons-round text-[20px] text-green-400 dark:text-green-600">{icon}</span>
        <span className="text-[13px] font-bold tracking-wide">{message}</span>
      </div>
    </div>
  );
};

export default Toast;