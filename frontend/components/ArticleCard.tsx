import React from 'react';
import { Article } from '../types';

interface ArticleCardProps {
  article: Article;
  variant?: 'featured' | 'standard' | 'grid';
  isBookmarked?: boolean;
  onToggleBookmark?: () => void;
  onClick?: () => void;
  loading?: boolean;
  showThumbnail?: boolean;
  onAiSummaryClick?: () => void;
}

const ArticleCard: React.FC<ArticleCardProps> = ({ 
  article, 
  variant = 'standard', 
  isBookmarked = false, 
  onToggleBookmark,
  onClick,
  loading = false,
  showThumbnail = true,
  onAiSummaryClick
}) => {
  const isFeatured = variant === 'featured';
  const isGrid = variant === 'grid';

  const handleBookmarkClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (onToggleBookmark) onToggleBookmark();
  };

  const handleAiSummaryClick = (e: React.MouseEvent) => {
      e.stopPropagation();
      if (onAiSummaryClick) onAiSummaryClick();
  };

  // --- SKELETON LOADER ---
  if (loading) {
    if (isFeatured || isGrid) {
        return (
            <div className={`rounded-[24px] bg-gray-200 dark:bg-white/10 animate-pulse ${isGrid ? 'aspect-[16/10] mb-4' : 'h-[420px] mx-5 mb-8'}`} />
        );
    }
    return (
        <div className="w-full pl-5 py-4 flex gap-4 border-b border-gray-100 dark:border-white/5">
            <div className="flex-1 space-y-3 pr-5">
                <div className="w-16 h-3 rounded-full bg-gray-200 dark:bg-white/10 animate-pulse" />
                <div className="w-full h-4 rounded bg-gray-200 dark:bg-white/10 animate-pulse" />
                <div className="w-3/4 h-4 rounded bg-gray-200 dark:bg-white/10 animate-pulse" />
            </div>
            {showThumbnail && (
                <div className="w-[80px] h-[80px] rounded-[16px] bg-gray-200 dark:bg-white/10 animate-pulse mr-5" />
            )}
        </div>
    );
  }

  // --- GRID VARIANT (Explore / Recommendations) ---
  if (isGrid) {
      return (
        <div onClick={onClick} className="group cursor-pointer flex flex-col h-full active:scale-[0.98] transition-transform duration-200">
            {/* Image */}
            <div className="w-full aspect-[4/3] mb-3 overflow-hidden rounded-[20px] bg-gray-100 dark:bg-white/5 shadow-sm border border-black/5 dark:border-white/5 relative">
                <img 
                    src={article.thumbnailUrl} 
                    alt={article.title}
                    className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                />
                <div className="absolute top-3 right-3 bg-black/50 backdrop-blur-md text-white text-[10px] font-bold px-2 py-1 rounded-md">
                    {article.readTime}
                </div>
            </div>
            
            {/* Meta */}
            <div className="flex items-center gap-2 mb-1.5 px-1">
                <span className="text-[11px] font-bold text-gray-500 dark:text-gray-400 uppercase tracking-wide truncate">
                    {article.source}
                </span>
            </div>

            {/* Title */}
            <h3 className="px-1 text-[18px] font-display font-bold text-slate-900 dark:text-white leading-snug mb-1 line-clamp-3 group-hover:text-primary transition-colors">
                {article.title}
            </h3>
            
            <div className="px-1 mt-auto flex justify-between items-center">
                <span className="text-[12px] text-gray-400 font-medium">
                    {article.date}
                </span>
                <button 
                    onClick={handleBookmarkClick}
                    className="p-1 text-gray-300 hover:text-primary transition-colors"
                >
                    <span className="material-icons-round text-[20px]">
                        {isBookmarked ? 'bookmark' : 'bookmark_border'}
                    </span>
                </button>
            </div>
        </div>
      );
  }

  // --- FEATURED CARD (Hero) ---
  if (isFeatured) {
    return (
      <div 
        onClick={onClick}
        className="group relative mx-5 mb-10 flex flex-col cursor-pointer active:scale-[0.98] transition-all duration-300"
      >
        <div className="relative w-full aspect-[4/5] sm:aspect-[16/9] rounded-[32px] overflow-hidden shadow-[0_20px_40px_-10px_rgba(0,0,0,0.15)] dark:shadow-none bg-slate-900">
          <img 
            src={article.thumbnailUrl} 
            alt={article.title}
            className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-105 opacity-90"
          />
          <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/20 to-transparent" />
          
          <div className="absolute top-0 left-0 p-6 sm:p-8 w-full flex justify-between items-start">
             <div className="inline-block bg-white/20 backdrop-blur-md border border-white/10 rounded-full px-3 py-1 text-[11px] font-bold uppercase tracking-wider text-white shadow-sm">
                Featured Story
             </div>
             
             <button 
                onClick={handleBookmarkClick}
                className="w-10 h-10 rounded-full bg-black/20 backdrop-blur-md flex items-center justify-center text-white hover:bg-black/40 transition-colors"
             >
                 <span className="material-icons-round text-[22px]">
                    {isBookmarked ? 'bookmark' : 'bookmark_border'}
                 </span>
             </button>
          </div>

           <div className="absolute bottom-0 left-0 right-0 p-6 sm:p-8 flex flex-col gap-3">
                <div className="flex items-center gap-2 mb-1">
                     <span className="text-white/80 text-[13px] font-bold tracking-wide uppercase">{article.source}</span>
                     <span className="text-white/40">•</span>
                     <span className="text-white/80 text-[13px] font-medium">{article.readTime}</span>
                </div>
                <h2 className="text-[32px] sm:text-[40px] leading-[1.1] font-display font-black text-white tracking-tight drop-shadow-md line-clamp-3">
                    {article.title}
                </h2>
                <p className="text-white/80 text-[16px] line-clamp-2 leading-relaxed font-medium max-w-2xl mt-2">
                    {article.excerpt}
                </p>
                
                {/* AI Summary Button on Featured Card */}
                <button 
                    onClick={handleAiSummaryClick}
                    className="self-start mt-2 flex items-center gap-2 px-4 py-2 bg-primary/90 backdrop-blur-md rounded-full text-white text-xs font-bold hover:bg-primary transition-colors"
                >
                    <span className="material-icons-round text-[16px]">auto_awesome</span>
                    AI Summary
                </button>
           </div>
        </div>
      </div>
    );
  }

  // --- STANDARD CARD (List Item - Matching Screenshot) ---
  return (
    <div 
        onClick={onClick}
        className="w-full bg-white dark:bg-black active:bg-gray-50 dark:active:bg-[#1C1C1E] transition-colors cursor-pointer group select-none"
    >
      <div className={`mx-5 py-5 flex items-start gap-4 border-b border-gray-100 dark:border-white/5`}>
        
        {/* Left: Text Content */}
        <div className="flex-1 flex flex-col min-w-0">
           
           {/* Header: Source & Date */}
           <div className="flex items-center justify-between mb-2">
                <span className="text-[11px] font-bold text-slate-500 dark:text-slate-400 uppercase tracking-wider">
                    {article.source}
                </span>
                <span className="text-[11px] text-slate-400 font-medium">
                    {article.date}
                </span>
           </div>
           
           {/* Title - Bold & Dark */}
           <h3 className="text-[17px] font-display font-bold text-[#0F172A] dark:text-white leading-[1.3] tracking-tight line-clamp-3 mb-3 group-hover:text-primary transition-colors">
             {article.title}
           </h3>
           
           {/* Footer: Tags, Time, Actions */}
           <div className="flex items-center gap-3 mt-1">
              {/* Tag Pill */}
              <span className="bg-slate-100 dark:bg-white/10 text-slate-600 dark:text-slate-300 px-2.5 py-1 rounded-[6px] text-[11px] font-bold">
                  {article.tags[0]}
              </span>

              {/* Read Time */}
              <span className="text-[11px] text-slate-400 font-medium whitespace-nowrap">
                  {article.readTime}
              </span>

              {/* Spacer */}
              <div className="flex-1" />

              {/* AI Summary Button */}
              <button 
                onClick={handleAiSummaryClick}
                className="w-8 h-8 flex items-center justify-center rounded-full text-primary hover:bg-violet-50 dark:hover:bg-primary/20 transition-colors"
                title="AI Summary"
              >
                  <span className="material-icons-round text-[18px]">auto_awesome</span>
              </button>

              {/* Bookmark Button */}
              <button 
                onClick={handleBookmarkClick}
                className={`w-8 h-8 flex items-center justify-center rounded-full transition-colors ${
                    isBookmarked 
                    ? 'text-primary' 
                    : 'text-slate-300 hover:text-slate-600 dark:text-slate-500 dark:hover:text-slate-300'
                }`}
                title="Save Article"
              >
                  <span className="material-icons-round text-[22px]">
                      {isBookmarked ? 'bookmark' : 'bookmark_border'}
                  </span>
              </button>
           </div>
        </div>

        {/* Right: Thumbnail Image */}
        {showThumbnail && (
            <div className="relative w-[100px] h-[100px] sm:w-[120px] sm:h-[100px] flex-shrink-0 rounded-[16px] overflow-hidden bg-gray-100 dark:bg-white/5 border border-black/5 dark:border-white/5 mt-1">
                <img 
                    src={article.thumbnailUrl} 
                    alt={article.title}
                    className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                />
            </div>
        )}
      </div>
    </div>
  );
};

export default ArticleCard;