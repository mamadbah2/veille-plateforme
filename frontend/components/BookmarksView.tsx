import React, { useState, useMemo, useRef } from 'react';
import ArticleCard from './ArticleCard';
import { Article } from '../types';

interface BookmarksViewProps {
  articles: Article[];
  onToggleBookmark: (id: string) => void;
  onNavigateHome: () => void;
  onArticleClick: (article: Article) => void;
  isLoading?: boolean;
}

// --- Swipeable Wrapper Component ---
const SwipeableArticleWrapper: React.FC<{
    children: React.ReactNode;
    onDelete: () => void;
}> = ({ children, onDelete }) => {
    const [offsetX, setOffsetX] = useState(0);
    const [isSwiping, setIsSwiping] = useState(false);
    const touchStartX = useRef<number | null>(null);
    const touchStartY = useRef<number | null>(null);

    const handleTouchStart = (e: React.TouchEvent) => {
        touchStartX.current = e.touches[0].clientX;
        touchStartY.current = e.touches[0].clientY;
    };

    const handleTouchMove = (e: React.TouchEvent) => {
        if (touchStartX.current === null || touchStartY.current === null) return;
        
        const currentX = e.touches[0].clientX;
        const currentY = e.touches[0].clientY;
        const diffX = currentX - touchStartX.current;
        const diffY = currentY - touchStartY.current;

        if (!isSwiping && Math.abs(diffX) > 10 && Math.abs(diffX) > Math.abs(diffY)) {
            setIsSwiping(true);
        }

        if (isSwiping) {
            if (diffX < 0) {
                setOffsetX(diffX);
            }
        }
    };

    const handleTouchEnd = () => {
        if (offsetX < -100) {
            setOffsetX(-window.innerWidth);
            setTimeout(() => {
                onDelete();
                setOffsetX(0); 
            }, 300);
        } else {
            setOffsetX(0);
        }
        setIsSwiping(false);
        touchStartX.current = null;
        touchStartY.current = null;
    };

    return (
        <div className="relative overflow-hidden mb-2">
             <div 
                className="absolute inset-y-0 right-0 w-24 bg-red-500 flex items-center justify-center text-white transition-opacity duration-300 rounded-r-xl"
                style={{ opacity: offsetX < 0 ? 1 : 0 }}
             >
                <span className="material-icons-round">delete</span>
             </div>

             <div 
                className="relative bg-white dark:bg-black touch-pan-y"
                style={{ 
                    transform: `translateX(${offsetX}px)`,
                    transition: isSwiping ? 'none' : 'transform 0.3s cubic-bezier(0.2, 0.8, 0.2, 1)'
                }}
                onTouchStart={handleTouchStart}
                onTouchMove={handleTouchMove}
                onTouchEnd={handleTouchEnd}
             >
                {children}
             </div>
        </div>
    );
};


const BookmarksView: React.FC<BookmarksViewProps> = ({ 
    articles, 
    onToggleBookmark, 
    onNavigateHome, 
    onArticleClick, 
    isLoading = false 
}) => {
  const [activeCategory, setActiveCategory] = useState('All');

  const availableCategories = useMemo(() => {
    const tags = new Set<string>();
    articles.forEach(article => {
      article.tags.forEach(tag => tags.add(tag));
    });
    return ['All', ...Array.from(tags).sort()];
  }, [articles]);

  const processedArticles = useMemo(() => {
    return articles.filter(article => {
      return activeCategory === 'All' ? true : article.tags.includes(activeCategory);
    });
  }, [articles, activeCategory]);

  const recentReads = useMemo(() => {
      return articles.slice(0, 2); // Simulating recent reads
  }, [articles]);

  if (isLoading) {
    return <div className="p-8 text-center text-zinc-400">Loading library...</div>;
  }

  if (articles.length === 0) {
      return (
        <div className="flex flex-col items-center justify-center h-full px-8 text-center animate-[fadeIn_0.5s_ease-out]">
            <div className="w-20 h-20 bg-zinc-100 dark:bg-white/5 rounded-full flex items-center justify-center mb-6">
                <span className="material-icons-round text-[32px] text-zinc-400">bookmark_border</span>
            </div>
            <h2 className="text-xl font-bold text-black dark:text-white mb-2">Your library is empty</h2>
            <p className="text-zinc-500 mb-8 max-w-xs">Articles you save will appear here. Start exploring to build your collection.</p>
            <button 
                onClick={onNavigateHome}
                className="px-8 py-3 bg-black dark:bg-white text-white dark:text-black rounded-full font-bold text-sm"
            >
                Explore
            </button>
        </div>
      );
  }

  return (
    <div className="flex flex-col h-full bg-white dark:bg-black relative">
      
      {/* Header */}
      <div className="sticky top-0 z-30 bg-white/95 dark:bg-black/95 backdrop-blur-xl border-b border-zinc-100 dark:border-white/5">
        <div className="px-6 pt-12 pb-6">
            <h1 className="text-[34px] font-black text-black dark:text-white tracking-tighter leading-none mb-6">
              Library
            </h1>

            {/* Filter Chips */}
            <div className="flex items-center gap-2 overflow-x-auto hide-scrollbar pb-1">
                {availableCategories.map(cat => (
                <button
                    key={cat}
                    onClick={() => setActiveCategory(cat)}
                    className={`flex-shrink-0 px-4 py-2 rounded-full text-[13px] font-bold border transition-all whitespace-nowrap ${
                    activeCategory === cat 
                        ? 'bg-black border-black text-white dark:bg-white dark:border-white dark:text-black' 
                        : 'bg-transparent border-zinc-200 dark:border-white/10 text-zinc-500 hover:border-zinc-300 dark:hover:border-white/30'
                    }`}
                >
                    {cat}
                </button>
                ))}
            </div>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto hide-scrollbar px-6 pb-28 pt-6">
        
        {/* Continue Reading Section (If 'All' is selected) */}
        {activeCategory === 'All' && recentReads.length > 0 && (
            <div className="mb-10">
                <div className="flex items-center gap-2 mb-4">
                     <span className="material-icons-round text-[18px] text-primary">schedule</span>
                     <h3 className="text-[12px] font-bold text-zinc-400 uppercase tracking-widest">Continue Reading</h3>
                </div>
                
                <div className="flex gap-4 overflow-x-auto hide-scrollbar pb-4 -mx-6 px-6">
                    {recentReads.map(article => (
                        <div 
                            key={`recent-${article.id}`} 
                            onClick={() => onArticleClick(article)}
                            className="flex-shrink-0 w-[280px] bg-zinc-50 dark:bg-white/5 rounded-2xl p-4 border border-zinc-100 dark:border-white/5 cursor-pointer hover:bg-zinc-100 dark:hover:bg-white/10 transition-colors"
                        >
                            <div className="flex justify-between items-start mb-3">
                                <span className="text-[10px] font-bold bg-white dark:bg-black/50 px-2 py-1 rounded text-zinc-600 dark:text-zinc-300">{article.source}</span>
                                <div className="w-6 h-6 rounded-full bg-zinc-200 dark:bg-white/10 flex items-center justify-center">
                                    <span className="material-icons-round text-[14px]">bookmark</span>
                                </div>
                            </div>
                            <h4 className="font-bold text-black dark:text-white leading-snug line-clamp-2 mb-3">
                                {article.title}
                            </h4>
                            <div className="w-full bg-zinc-200 dark:bg-white/10 h-1 rounded-full overflow-hidden">
                                <div className="bg-primary h-full w-[40%] rounded-full"></div>
                            </div>
                            <span className="text-[10px] text-zinc-400 mt-1.5 block">4 min left</span>
                        </div>
                    ))}
                </div>
            </div>
        )}

        {/* Main List */}
        <div>
            <div className="flex items-center justify-between mb-4">
                <h3 className="text-[12px] font-bold text-zinc-400 uppercase tracking-widest">
                    {activeCategory === 'All' ? 'Saved Articles' : activeCategory}
                </h3>
                <span className="text-[12px] font-bold text-zinc-400">{processedArticles.length}</span>
            </div>

            <div className="space-y-4">
                {processedArticles.map((article) => (
                    <SwipeableArticleWrapper 
                        key={article.id} 
                        onDelete={() => onToggleBookmark(article.id)}
                    >
                        <div 
                            onClick={() => onArticleClick(article)}
                            className="flex gap-4 p-4 rounded-2xl border border-zinc-100 dark:border-white/5 hover:border-zinc-300 dark:hover:border-white/20 transition-all cursor-pointer bg-white dark:bg-black group"
                        >
                             <div className="w-20 h-20 rounded-xl bg-zinc-100 dark:bg-white/10 flex-shrink-0 overflow-hidden">
                                 <img src={article.thumbnailUrl} className="w-full h-full object-cover grayscale group-hover:grayscale-0 transition-all" alt="" />
                             </div>
                             <div className="flex-1 min-w-0 flex flex-col justify-center">
                                 <div className="flex items-center gap-2 mb-1">
                                    <img src={article.author.avatarUrl} className="w-4 h-4 rounded-full" alt=""/>
                                    <span className="text-[11px] font-bold text-zinc-500">{article.author.name}</span>
                                 </div>
                                 <h3 className="text-[16px] font-bold text-black dark:text-white leading-snug line-clamp-2 mb-1">
                                     {article.title}
                                 </h3>
                                 <span className="text-[11px] text-zinc-400">{article.readTime} • {article.date}</span>
                             </div>
                        </div>
                    </SwipeableArticleWrapper>
                ))}
            </div>
        </div>
      </div>
    </div>
  );
};

export default BookmarksView;