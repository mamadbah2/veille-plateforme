import React, { useState, useEffect, useMemo, useRef } from 'react';
import { Article } from '../types';

interface SearchOverlayProps {
  onClose: () => void;
  isOpen: boolean;
  articles?: Article[];
  onArticleClick?: (article: Article) => void;
}

// --- BENTO GRID CONFIGURATION ---
// Une grille 3 colonnes sur Desktop, 2 colonnes sur Mobile.
// L'agencement est optimisé pour éviter les trous sur mobile.
const BENTO_ITEMS = [
  { 
    id: 'frontend', 
    title: 'Frontend', 
    subtitle: 'React, CSS, Web', 
    // Mobile: Full width square (2x2 relative to small grid). Desktop: 2x2.
    className: 'col-span-2 row-span-2', 
    image: 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&q=80&w=600',
    textColor: 'text-white'
  },
  { 
    id: 'design', 
    title: 'Design', 
    subtitle: 'UI/UX & Motion', 
    // Mobile: 1x2 (Vertical strip). Desktop: 1x2.
    // In a 2-col grid, this sits nicely next to two 1x1 items stacked, or another 1x2.
    className: 'col-span-1 row-span-2', 
    image: 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&q=80&w=400',
    textColor: 'text-white'
  },
  { 
    id: 'backend', 
    title: 'Backend', 
    subtitle: 'Server & DB', 
    // Mobile: 1x1. Desktop: 1x1.
    className: 'col-span-1 row-span-1', 
    image: 'https://images.unsplash.com/photo-1558494949-ef526b0042a0?auto=format&fit=crop&q=80&w=400',
    textColor: 'text-white'
  },
  { 
    id: 'mobile', 
    title: 'Mobile', 
    subtitle: 'iOS & Android', 
    // Mobile: 1x1. Desktop: 1x1.
    className: 'col-span-1 row-span-1', 
    image: 'https://images.unsplash.com/photo-1512941937669-90a1b58e7e9c?auto=format&fit=crop&q=80&w=400',
    textColor: 'text-white'
  },
  { 
    id: 'ai', 
    title: 'AI & ML', 
    subtitle: 'LLMs & Agents', 
    // Mobile: 1x1. Desktop: 1x1.
    className: 'col-span-1 row-span-1', 
    image: 'https://images.unsplash.com/photo-1677442136019-21780ecad995?auto=format&fit=crop&q=80&w=400',
    textColor: 'text-white'
  },
];

const TRENDING_TOPICS = [
  'React Server Components', 
  'Tailwind CSS v4', 
  'Local LLMs', 
  'Rust for Web', 
  'Micro-frontends', 
  'VisionOS Design'
];

const RECENT_SEARCHES = ['Next.js 14', 'Kubernetes', 'Clean Architecture'];

const SearchOverlay: React.FC<SearchOverlayProps> = ({ onClose, isOpen, articles = [], onArticleClick }) => {
  const [isVisible, setIsVisible] = useState(false);
  const [query, setQuery] = useState('');
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (isOpen) {
      setIsVisible(true);
      setTimeout(() => inputRef.current?.focus(), 50);
      document.body.style.overflow = 'hidden';
    } else {
      const timer = setTimeout(() => setIsVisible(false), 200);
      document.body.style.overflow = 'unset';
      return () => clearTimeout(timer);
    }
  }, [isOpen]);

  // Handle ESC
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [onClose]);

  const results = useMemo(() => {
    if (!query.trim()) return [];
    const lowerQ = query.toLowerCase();
    return articles.filter(a => 
      a.title.toLowerCase().includes(lowerQ) || 
      a.author.name.toLowerCase().includes(lowerQ) ||
      a.tags.some(t => t.toLowerCase().includes(lowerQ))
    );
  }, [query, articles]);

  if (!isOpen && !isVisible) return null;

  // Z-Index is set to 50, allowing the BottomNav (z-101) to sit on top of it on mobile
  return (
    <div className={`fixed inset-0 z-[50] flex flex-col sm:items-center sm:pt-[8vh] transition-all duration-200 ${
        isOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'
    }`}>
      
      {/* Backdrop (Blur) - Reduced opacity and adjusted blur as requested */}
      <div 
        className="absolute inset-0 bg-black/20 dark:bg-black/40 backdrop-blur-sm transition-opacity duration-200" 
        onClick={onClose}
      />

      {/* Container */}
      <div className={`
        relative w-full h-full sm:h-auto sm:max-h-[700px] sm:max-w-[680px] 
        bg-white dark:bg-[#1C1C1E] 
        sm:rounded-[24px] shadow-2xl overflow-hidden flex flex-col
        transition-transform duration-300 ease-[cubic-bezier(0.16,1,0.3,1)]
        ${isOpen ? 'translate-y-0 scale-100' : 'translate-y-4 scale-95'}
      `}>
        
        {/* Search Header */}
        <div className="flex items-center px-5 py-5 border-b border-gray-100 dark:border-white/5 bg-white/80 dark:bg-[#1C1C1E]/80 backdrop-blur-md sticky top-0 z-20">
            <span className="material-icons-round text-[24px] text-gray-400">search</span>
            <input 
                ref={inputRef}
                type="text" 
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Find articles, topics..." 
                className="flex-1 h-10 bg-transparent border-none outline-none text-[22px] text-gray-900 dark:text-white placeholder-gray-300 px-3 font-semibold tracking-tight"
            />
            {query && (
                <button onClick={() => setQuery('')} className="p-2 text-gray-400 hover:text-gray-900 dark:hover:text-white transition-colors">
                    <span className="material-icons-round text-[20px]">close</span>
                </button>
            )}
            <button 
                onClick={onClose}
                className="ml-3 px-3 py-1.5 bg-gray-100 dark:bg-white/10 rounded-lg text-[13px] font-semibold text-gray-600 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-white/20 transition-colors"
            >
                Esc
            </button>
        </div>

        {/* Scrollable Content - Added pb-28 to clear the floating mobile nav */}
        <div className="flex-1 overflow-y-auto hide-scrollbar p-6 pb-28">
            
            {query ? (
                /* RESULTS VIEW */
                <div className="space-y-2 animate-[fadeIn_0.1s_ease-out]">
                    <div className="px-2 mb-2 text-[11px] font-bold text-gray-400 uppercase tracking-wider">Matches</div>
                    {results.length > 0 ? (
                        results.map(article => (
                            <div 
                                key={article.id}
                                onClick={() => {
                                    onArticleClick?.(article);
                                    onClose();
                                }}
                                className="group flex items-center gap-4 p-3 rounded-xl hover:bg-gray-100 dark:hover:bg-white/10 transition-colors cursor-pointer"
                            >
                                <div className="w-12 h-12 rounded-lg bg-gray-200 dark:bg-white/10 overflow-hidden flex-shrink-0">
                                    <img src={article.thumbnailUrl} alt="" className="w-full h-full object-cover" />
                                </div>
                                <div className="flex-1 min-w-0">
                                    <h4 className="text-[16px] font-semibold text-gray-900 dark:text-white truncate">
                                        {article.title}
                                    </h4>
                                    <p className="text-[13px] text-gray-500 dark:text-gray-400">
                                        {article.author.name}
                                    </p>
                                </div>
                                <span className="material-icons-round text-[18px] text-gray-300 group-hover:text-gray-500">chevron_right</span>
                            </div>
                        ))
                    ) : (
                        <div className="py-12 text-center text-gray-400">No results found</div>
                    )}
                </div>
            ) : (
                /* DASHBOARD VIEW */
                <div className="space-y-8 animate-[fadeIn_0.2s_ease-out]">
                    
                    {/* 1. Bento Grid Categories (Images + Text) */}
                    <div>
                        {/* 
                            Grid Layout Logic:
                            - Mobile (Default): 2 cols, rows are 100px tall (compact).
                            - Desktop (sm+): 3 cols, rows are 120px tall.
                        */}
                        <div className="grid grid-cols-2 sm:grid-cols-3 gap-3 auto-rows-[100px] sm:auto-rows-[120px]">
                            {BENTO_ITEMS.map((item) => (
                                <button 
                                    key={item.id}
                                    onClick={() => setQuery(item.title)}
                                    className={`relative group rounded-2xl overflow-hidden flex flex-col justify-end items-start text-left transition-all duration-300 hover:scale-[1.02] active:scale-[0.98] ${item.className} shadow-sm`}
                                >
                                    {/* Background Image */}
                                    <img 
                                        src={item.image} 
                                        alt={item.title} 
                                        className="absolute inset-0 w-full h-full object-cover transition-transform duration-700 group-hover:scale-110"
                                    />
                                    
                                    {/* Gradient Overlay for Text Readability */}
                                    <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/30 to-transparent group-hover:from-black/90 transition-colors"></div>
                                    
                                    <div className="relative z-10 p-4 w-full">
                                        <h3 className={`text-[20px] font-bold leading-tight ${item.textColor} tracking-tight drop-shadow-sm`}>
                                            {item.title}
                                        </h3>
                                        <p className={`text-[12px] font-medium opacity-90 mt-0.5 ${item.textColor} drop-shadow-sm`}>
                                            {item.subtitle}
                                        </p>
                                    </div>
                                </button>
                            ))}
                        </div>
                    </div>

                    {/* 2. Trending Topics (Just Text) */}
                    <div>
                        <h3 className="px-1 text-[11px] font-bold text-gray-400 uppercase tracking-wider mb-3">Trending Now</h3>
                        <div className="flex flex-wrap gap-2">
                            {TRENDING_TOPICS.map(topic => (
                                <button 
                                    key={topic}
                                    onClick={() => setQuery(topic)}
                                    className="px-3.5 py-1.5 rounded-lg border border-gray-200 dark:border-white/10 text-[13px] font-semibold text-gray-600 dark:text-gray-300 hover:border-gray-300 dark:hover:border-white/30 hover:bg-gray-50 dark:hover:bg-white/5 transition-all"
                                >
                                    {topic}
                                </button>
                            ))}
                        </div>
                    </div>

                    {/* 3. Recent Searches */}
                    <div>
                        <h3 className="px-1 text-[11px] font-bold text-gray-400 uppercase tracking-wider mb-2">Recent</h3>
                        <div className="space-y-1">
                            {RECENT_SEARCHES.map(term => (
                                <button 
                                    key={term}
                                    onClick={() => setQuery(term)}
                                    className="w-full flex items-center gap-3 px-2 py-2.5 rounded-lg hover:bg-gray-100 dark:hover:bg-white/5 text-left transition-colors group"
                                >
                                    <span className="material-icons-round text-[18px] text-gray-300 group-hover:text-primary transition-colors">history</span>
                                    <span className="text-[14px] text-gray-600 dark:text-gray-300 font-medium">
                                        {term}
                                    </span>
                                </button>
                            ))}
                        </div>
                    </div>

                </div>
            )}
        </div>
        
    </div>
  </div>
  );
};

export default SearchOverlay;