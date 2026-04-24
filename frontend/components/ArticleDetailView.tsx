import React, { useEffect, useState, useRef, useMemo } from 'react';
import { Article, TextSize } from '../types';

interface ArticleDetailViewProps {
  article: Article;
  allArticles?: Article[];
  onClose: () => void;
  isBookmarked: boolean;
  onToggleBookmark: () => void;
  textSize: TextSize;
  onShowToast?: (msg: string, icon?: string) => void;
  onArticleClick?: (article: Article) => void;
}

const ArticleDetailView: React.FC<ArticleDetailViewProps> = ({ 
    article, 
    allArticles = [],
    onClose, 
    isBookmarked, 
    onToggleBookmark, 
    textSize: initialTextSize, 
    onShowToast,
    onArticleClick
}) => {
  const [isVisible, setIsVisible] = useState(false);
  const [scrollProgress, setScrollProgress] = useState(0);
  const [showStickyHeader, setShowStickyHeader] = useState(false);
  const [isBottomBarVisible, setIsBottomBarVisible] = useState(true);
  const lastScrollTop = useRef(0);

  // Appearance Settings
  const [localTextSize, setLocalTextSize] = useState<TextSize>(initialTextSize);
  const [showAppearanceMenu, setShowAppearanceMenu] = useState(false);
  const [localTheme, setLocalTheme] = useState<'system' | 'light' | 'sepia' | 'dark'>('system');

  const scrollerRef = useRef<HTMLDivElement>(null);

  // Scroll to top when article changes
  useEffect(() => {
    if (scrollerRef.current) {
        scrollerRef.current.scrollTop = 0;
    }
    // Reset visual states
    setShowStickyHeader(false);
    setIsBottomBarVisible(true);
  }, [article.id]);

  useEffect(() => {
    requestAnimationFrame(() => setIsVisible(true));
    document.body.style.overflow = 'hidden';
    return () => { document.body.style.overflow = 'unset'; }
  }, []);

  // Calculate Related Articles
  const relatedArticles = useMemo(() => {
    let related = allArticles
        .filter(a => a.id !== article.id && a.tags.some(t => article.tags.includes(t)));
    
    if (related.length < 2) {
        const remaining = allArticles.filter(a => a.id !== article.id && !related.find(r => r.id === a.id));
        related = [...related, ...remaining];
    }

    return related.slice(0, 2);
  }, [article, allArticles]);

  const handleClose = () => {
    setIsVisible(false);
    setTimeout(onClose, 300);
  };

  const handleOpenSource = () => {
      if (article.url) {
          window.open(article.url, '_blank', 'noopener,noreferrer');
      }
  };

  const handleShare = async () => {
    if (navigator.share) {
        try {
            await navigator.share({
                title: article.title,
                text: article.excerpt,
                url: article.url || window.location.href,
            });
            if (onShowToast) onShowToast('Shared successfully', 'ios_share');
        } catch (error) {
            console.log('Error sharing', error);
        }
    } else {
        if (onShowToast) onShowToast('Link copied', 'content_copy');
    }
  };

  const handleScroll = (e: React.UIEvent<HTMLDivElement>) => {
    const { scrollTop, scrollHeight, clientHeight } = e.currentTarget;
    const progress = (scrollTop / (scrollHeight - clientHeight)) * 100;
    setScrollProgress(progress);
    
    // Header transition logic
    const showHeaderThreshold = 200; // Show solid header after scrolling past image area roughly
    setShowStickyHeader(scrollTop > showHeaderThreshold);

    // Auto-hide bottom bar logic
    const diff = scrollTop - lastScrollTop.current;
    const isScrollingDown = diff > 0;
    const isScrollingUp = diff < 0;

    if (Math.abs(diff) > 10) {
        if (isScrollingDown && scrollTop > 150) {
            setIsBottomBarVisible(false);
        } else if (isScrollingUp) {
            setIsBottomBarVisible(true);
        }
        lastScrollTop.current = scrollTop;
    }

    if (scrollTop < 50) setIsBottomBarVisible(true);
    if (scrollHeight - scrollTop - clientHeight < 50) setIsBottomBarVisible(true);
    
    if (showAppearanceMenu) setShowAppearanceMenu(false);
  };

  const getProseClass = () => {
    switch (localTextSize) {
      case 'large': return 'prose-lg md:prose-xl';
      case 'extra': return 'prose-xl md:prose-2xl';
      default: return 'prose-base md:prose-lg';
    }
  };

  const getThemeClass = () => {
      switch(localTheme) {
          case 'sepia': return 'bg-[#F9F5EC] text-[#3E3A32]';
          case 'dark': return 'bg-black text-white'; 
          case 'light': return 'bg-white text-black';
          default: return 'bg-white dark:bg-black text-black dark:text-white';
      }
  };

  // Dynamic colors based on header state
  const headerIconClass = showStickyHeader 
      ? (localTheme === 'dark' ? 'text-white' : 'text-slate-900') 
      : 'text-white drop-shadow-md';
  
  const headerBgClass = showStickyHeader
      ? (localTheme === 'dark' ? 'bg-[#1C1C1E]/90 border-b border-white/5' : 'bg-white/90 border-b border-slate-200')
      : 'bg-transparent';

  return (
    <div 
        className={`fixed inset-0 z-[200] flex flex-col transition-transform duration-300 ease-[cubic-bezier(0.2,0.8,0.2,1)] ${getThemeClass()} ${
            isVisible ? 'translate-y-0' : 'translate-y-full'
        }`}
    >
        {/* Progress Bar (Attached to Top) */}
        <div className="absolute top-0 left-0 right-0 h-1 z-[220]">
            <div className="h-full bg-primary transition-all duration-150" style={{ width: `${scrollProgress}%` }} />
        </div>

        {/* Adaptive Sticky Header */}
        <div className={`absolute top-0 left-0 right-0 z-[210] transition-all duration-300 backdrop-blur-md ${headerBgClass}`}>
            <div className="flex items-center justify-between px-4 pt-[calc(env(safe-area-inset-top)+10px)] pb-3">
                {/* Back Button */}
                <button 
                    onClick={handleClose}
                    className={`w-10 h-10 rounded-full flex items-center justify-center transition-colors ${
                        showStickyHeader ? 'hover:bg-black/5 dark:hover:bg-white/10' : 'bg-black/20 hover:bg-black/30 backdrop-blur-md border border-white/10'
                    }`}
                >
                    <span className={`material-icons-round text-[24px] ${headerIconClass}`}>arrow_back</span>
                </button>

                {/* Title in Header (Fade in) */}
                <div className={`flex-1 mx-4 text-center transition-opacity duration-300 ${showStickyHeader ? 'opacity-100' : 'opacity-0'}`}>
                    <span className={`text-[13px] font-bold uppercase tracking-wider block opacity-60`}>
                        {article.source}
                    </span>
                </div>

                {/* Appearance Settings */}
                <div className="relative">
                    <button 
                        onClick={() => setShowAppearanceMenu(!showAppearanceMenu)}
                        className={`w-10 h-10 rounded-full flex items-center justify-center transition-colors ${
                            showStickyHeader ? 'hover:bg-black/5 dark:hover:bg-white/10' : 'bg-black/20 hover:bg-black/30 backdrop-blur-md border border-white/10'
                        }`}
                    >
                        <span className={`material-icons-round text-[22px] ${headerIconClass}`}>text_fields</span>
                    </button>

                    {/* Popover Menu */}
                    {showAppearanceMenu && (
                        <div className="absolute top-12 right-0 w-64 bg-[#F2F2F7] dark:bg-[#1C1C1E] rounded-xl shadow-2xl border border-white/20 p-4 animate-[fadeInUp_0.2s_ease-out] ring-1 ring-black/5 z-50">
                            <div className="flex justify-between items-center bg-white dark:bg-[#2C2C2E] rounded-[10px] p-1 mb-4 shadow-sm">
                                <button onClick={() => setLocalTextSize('normal')} className="flex-1 py-1.5 text-[12px] font-bold text-slate-500 hover:text-black dark:text-slate-400 dark:hover:text-white transition-colors">A</button>
                                <button onClick={() => setLocalTextSize('large')} className="flex-1 py-1.5 text-[16px] font-bold text-black dark:text-white border-x border-[#E5E5EA] dark:border-black/20">A</button>
                                <button onClick={() => setLocalTextSize('extra')} className="flex-1 py-1.5 text-[20px] font-bold text-black dark:text-white">A</button>
                            </div>
                            <div className="flex justify-between gap-3">
                                <button onClick={() => setLocalTheme('light')} className={`flex-1 h-10 rounded-full border border-[#C6C6C8] bg-white ${localTheme === 'light' ? 'ring-2 ring-primary border-primary' : ''}`} title="Light" />
                                <button onClick={() => setLocalTheme('sepia')} className={`flex-1 h-10 rounded-full border border-[#EBE1CF] bg-[#F9F5EC] ${localTheme === 'sepia' ? 'ring-2 ring-primary border-primary' : ''}`} title="Sepia" />
                                <button onClick={() => setLocalTheme('dark')} className={`flex-1 h-10 rounded-full border border-[#38383A] bg-[#1C1C1E] ${localTheme === 'dark' ? 'ring-2 ring-primary border-primary' : ''}`} title="Dark" />
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>

        {/* Scroll Container */}
        <div 
            id="article-scroller"
            ref={scrollerRef}
            className="flex-1 overflow-y-auto hide-scrollbar pb-32" 
            onScroll={handleScroll}
        >
            {/* Immersive Hero Image */}
            <div className="relative w-full h-[50vh] md:h-[60vh]">
                <img 
                    src={article.thumbnailUrl} 
                    alt={article.title}
                    className="w-full h-full object-cover"
                />
                <div className="absolute inset-0 bg-gradient-to-b from-black/60 via-transparent to-black/60" />
                
                {/* Title Block on Image */}
                <div className="absolute bottom-0 left-0 right-0 p-6 md:p-12 max-w-4xl mx-auto w-full pb-12">
                     <div className="flex items-center gap-3 mb-4 animate-[fadeInUp_0.5s_ease-out]">
                        <span className="bg-primary/90 backdrop-blur-md px-3 py-1 rounded-full text-white text-[10px] font-bold uppercase tracking-wider shadow-sm">
                            {article.tags[0]}
                        </span>
                        <span className="text-white/90 text-[13px] font-medium drop-shadow-md flex items-center gap-1">
                            <span className="material-icons-round text-[14px]">schedule</span>
                            {article.readTime}
                        </span>
                     </div>
                     <h1 className="text-[32px] md:text-[52px] font-black text-white leading-[1.1] tracking-tight drop-shadow-lg animate-[fadeInUp_0.6s_ease-out]">
                         {article.title}
                     </h1>
                </div>
            </div>

            {/* Article Body */}
            <div className="px-5 md:px-0 max-w-2xl mx-auto -mt-6 relative z-10">
                
                {/* Meta Card */}
                <div className={`p-6 rounded-2xl shadow-lg mb-10 flex items-center justify-between border ${
                    localTheme === 'dark' 
                    ? 'bg-[#1C1C1E] border-white/10' 
                    : (localTheme === 'sepia' ? 'bg-[#FDFBF7] border-[#EBE1CF]' : 'bg-white border-slate-100')
                }`}>
                    <div className="flex items-center gap-3">
                        <div className="w-12 h-12 rounded-full overflow-hidden ring-2 ring-slate-100 dark:ring-white/10">
                            <img src={article.author.avatarUrl} alt={article.author.name} className="w-full h-full object-cover"/>
                        </div>
                        <div>
                            <p className="text-[15px] font-bold leading-tight">{article.author.name}</p>
                            <p className="text-[12px] opacity-60 font-medium">{article.source} • {article.date}</p>
                        </div>
                    </div>
                    {/* Follow/Action Button (Visual only) */}
                    <button className="text-primary font-bold text-[13px] hover:opacity-80">
                        Follow
                    </button>
                </div>

                {/* Typography Content */}
                <div className={`prose ${getProseClass()} ${localTheme === 'sepia' ? 'prose-brown' : 'prose-slate dark:prose-invert'} max-w-none`}>
                    <p className="lead font-serif text-[1.2em] leading-[1.6] opacity-90 mb-8 font-medium">
                        {article.excerpt}
                    </p>
                    
                    <p className="opacity-90 mb-6 leading-[1.8]">
                        Traditionally, developers have had to rely on complex hacks and workarounds to achieve what should be simple layout requirements. However, with the new specifications landing in modern browsers, we are seeing a paradigm shift.
                    </p>

                    <h3 className="text-[1.5em] font-bold opacity-100 mb-4 mt-12 tracking-tight">The Core Problem</h3>
                    
                    <p className="opacity-90 mb-6 leading-[1.8]">
                        React 19 introduces automatic memoization via the new compiler. Here is how it changes your development workflow. This is just the beginning of a deep dive into the technology that is changing the landscape of web development.
                    </p>

                    <figure className="my-10 -mx-5 md:-mx-10">
                         <img src="https://images.unsplash.com/photo-1555099962-4199c345e5dd?auto=format&fit=crop&q=80&w=1200" alt="Code snippet" className="w-full md:rounded-xl shadow-xl" />
                         <figcaption className="text-center text-sm opacity-60 mt-3 italic">Figure 1: The evolution of state management in 2024.</figcaption>
                    </figure>

                    <h3 className="text-[1.5em] font-bold opacity-100 mb-4 mt-12 tracking-tight">Looking Ahead</h3>
                    <p className="opacity-90 mb-6 leading-[1.8]">
                        As we wrap up this analysis, remember that tools are only as good as the craftsperson using them. Stay curious, keep learning, and don't be afraid to break things in your development environment.
                    </p>
                </div>

                <div className="my-12 h-px bg-slate-200 dark:bg-white/10 w-full" />

                {/* Related Articles */}
                <h3 className="text-[13px] font-bold uppercase tracking-widest opacity-60 mb-6">Read Next</h3>
                <div className="grid grid-cols-1 gap-6 mb-20">
                     {relatedArticles.map((relArticle) => (
                        <div 
                            key={relArticle.id}
                            onClick={() => onArticleClick?.(relArticle)}
                            className={`flex gap-4 p-4 rounded-xl border cursor-pointer active:scale-[0.99] transition-transform ${
                                localTheme === 'dark' 
                                ? 'bg-[#1C1C1E] border-white/5 hover:bg-white/5' 
                                : (localTheme === 'sepia' ? 'bg-[#FDFBF7] border-[#EBE1CF] hover:bg-white' : 'bg-white border-slate-100 hover:bg-slate-50')
                            }`}
                        >
                            <div className="w-24 h-24 rounded-lg bg-slate-200 overflow-hidden flex-shrink-0">
                                <img src={relArticle.thumbnailUrl} className="w-full h-full object-cover" alt="" />
                            </div>
                            <div className="flex-1 min-w-0 flex flex-col justify-center">
                                <span className="text-[10px] font-bold uppercase tracking-wide opacity-50 mb-1">{relArticle.source}</span>
                                <h4 className="text-[16px] font-bold leading-snug line-clamp-2 mb-1">{relArticle.title}</h4>
                                <span className="text-[11px] opacity-60">{relArticle.readTime}</span>
                            </div>
                        </div>
                     ))}
                </div>

            </div>
        </div>

        {/* Bottom Floating Action Bar */}
        <div 
            className={`absolute bottom-8 left-0 right-0 z-[210] flex justify-center transition-all duration-300 ${
                isBottomBarVisible ? 'translate-y-0 opacity-100' : 'translate-y-20 opacity-0 pointer-events-none'
            }`}
        >
            <div className={`flex items-center gap-1 p-2 rounded-full shadow-2xl border ${
                 localTheme === 'dark' 
                 ? 'bg-[#2C2C2E] border-white/10' 
                 : (localTheme === 'sepia' ? 'bg-[#FDFBF7] border-[#EBE1CF]' : 'bg-white border-slate-200')
            }`}>
                 <button 
                    onClick={handleShare}
                    className="w-12 h-12 rounded-full flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/10 transition-colors"
                    title="Share"
                 >
                    <span className="material-icons-round text-[22px]">ios_share</span>
                 </button>
                 
                 <div className="w-px h-6 bg-slate-200 dark:bg-white/10 mx-1"></div>

                 <button 
                    onClick={onToggleBookmark}
                    className="w-12 h-12 rounded-full flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/10 transition-colors"
                    title={isBookmarked ? "Remove Bookmark" : "Bookmark"}
                 >
                    <span className={`material-icons-round text-[24px] ${isBookmarked ? 'text-primary' : ''}`}>
                        {isBookmarked ? 'bookmark' : 'bookmark_border'}
                    </span>
                 </button>

                 <div className="w-px h-6 bg-slate-200 dark:bg-white/10 mx-1"></div>

                 <button 
                    onClick={handleOpenSource}
                    className="w-12 h-12 rounded-full flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/10 transition-colors"
                    title="Open in Browser"
                 >
                    <span className="material-icons-round text-[22px]">public</span>
                 </button>
            </div>
        </div>

    </div>
  );
};

export default ArticleDetailView;