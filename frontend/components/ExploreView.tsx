import React, { useState, useMemo } from 'react';
import { Article } from '../types';
import ArticleCard from './ArticleCard';

interface ExploreViewProps {
  articles: Article[];
  onArticleClick: (article: Article) => void;
  onOpenSearch?: () => void;
  onSeeMore?: (topic: string) => void;
}

const ALL_TOPICS = [
    'Society', 'Culture', 'Politics', 'Psychology', 'Mental Health', 'Philosophy', 'Writing', 'Self Improvement', 'Relationships',
    'Technology', 'Science', 'Programming', 'Art', 'History', 'Humor', 'Business', 'Marketing', 'Cryptocurrency'
];

// Configuration for the Bento Grid Layout
// Using row/col spans relative to a 4-column grid on desktop, 2-column on mobile.
const FEATURED_CATEGORIES = [
    { 
        id: 'Technology', 
        label: 'Technology', 
        image: 'https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&q=80&w=800', 
        // Mobile: Full width (2 cols), Tall. Desktop: 2 cols, 2 rows.
        className: 'col-span-2 row-span-2'
    },
    { 
        id: 'Science', 
        label: 'Science', 
        image: 'https://images.unsplash.com/photo-1507668017186-a994307ecec3?auto=format&fit=crop&q=80&w=600', 
        // Mobile: 1 col. Desktop: 1 col, 1 row.
        className: 'col-span-1 row-span-1'
    },
    { 
        id: 'Culture', 
        label: 'Culture', 
        image: 'https://images.unsplash.com/photo-1533158326339-b1045247901d?auto=format&fit=crop&q=80&w=600', 
        // Mobile: 1 col. Desktop: 1 col, 1 row.
        className: 'col-span-1 row-span-1'
    },
    { 
        id: 'Design', 
        label: 'Design', 
        image: 'https://images.unsplash.com/photo-1561070791-2526d30994b5?auto=format&fit=crop&q=80&w=600', 
        // Mobile: 1 col. Desktop: 1 col, 1 row.
        className: 'col-span-1 row-span-1'
    },
    { 
        id: 'Psychology', 
        label: 'Psychology', 
        image: 'https://images.unsplash.com/photo-1471970471555-19d4b113e9ed?auto=format&fit=crop&q=80&w=600', 
        // Mobile: 1 col. Desktop: 1 col, 1 row.
        className: 'col-span-1 row-span-1'
    },
    { 
        id: 'Politics', 
        label: 'Politics', 
        image: 'https://images.unsplash.com/photo-1529101091760-61df6be5d18b?auto=format&fit=crop&q=80&w=800', 
        // Mobile: Full width. Desktop: Full width (4 cols), 1 row (slender).
        className: 'col-span-2 md:col-span-4 row-span-1' 
    },
];

const TOPIC_METADATA: Record<string, { desc?: string; followers: string; stories: string }> = {
    'Society': { followers: '3.1M', stories: '128K' },
    'Culture': { followers: '4.2M', stories: '210K' },
    'Politics': { followers: '2.8M', stories: '150K' },
    'Technology': { followers: '5.5M', stories: '320K' },
    'Programming': { followers: '2.1M', stories: '95K' },
    'Science': { followers: '4.8M', stories: '180K' },
    'Design': { followers: '3.5M', stories: '145K' },
    'Psychology': { followers: '2.9M', stories: '110K' },
    'default': { followers: '100K+', stories: '10K+' }
};

const ExploreView: React.FC<ExploreViewProps> = ({ articles, onArticleClick, onOpenSearch, onSeeMore }) => {
  const [selectedTopic, setSelectedTopic] = useState<string | null>(null);
  const [isFollowingTopic, setIsFollowingTopic] = useState(false);
  const [isLoadingMore, setIsLoadingMore] = useState(false);

  const currentTopicMeta = selectedTopic ? (TOPIC_METADATA[selectedTopic] || TOPIC_METADATA['default']) : TOPIC_METADATA['default'];

  // Mock filtered articles
  const recommendedArticles = useMemo(() => articles.slice(0, 2), [articles]);
  const listArticles = useMemo(() => articles.slice(2), [articles]);

  const handleLoadMore = () => {
      setIsLoadingMore(true);
      setTimeout(() => {
          setIsLoadingMore(false);
          if (onSeeMore && selectedTopic) {
              onSeeMore(selectedTopic);
          }
      }, 800);
  };

  // --- RENDER: DASHBOARD ---
  if (!selectedTopic) {
      return (
        <div className="flex flex-col h-full bg-white dark:bg-black font-sans relative">
            <div className="flex-1 overflow-y-auto hide-scrollbar px-5 pt-12 pb-24">
                
                <div className="max-w-4xl mx-auto">
                    <h1 className="text-[34px] md:text-[42px] font-black text-black dark:text-white mb-8 tracking-tighter">
                        Explore
                    </h1>

                    {/* Fake Search Input Trigger */}
                    <div className="relative group mb-12 cursor-pointer" onClick={onOpenSearch}>
                        <span className="absolute left-4 top-1/2 -translate-y-1/2 text-zinc-400 group-hover:text-primary transition-colors">
                            <span className="material-icons-round text-[24px]">search</span>
                        </span>
                        <div className="w-full h-14 bg-zinc-100 dark:bg-zinc-900 border border-transparent group-hover:border-primary/20 group-hover:bg-white dark:group-hover:bg-white/5 rounded-2xl pl-12 pr-4 flex items-center text-[16px] text-zinc-500 font-medium transition-all shadow-sm group-hover:shadow-md">
                            Topics, people, stories...
                        </div>
                    </div>

                    {/* BENTO GRID */}
                    <section className="mb-14 animate-[fadeIn_0.5s_ease-out]">
                        <div className="flex items-center justify-between mb-6">
                            <h3 className="text-[18px] font-bold text-black dark:text-white">
                                Browse by Category
                            </h3>
                            <button className="text-[13px] font-bold text-primary hover:opacity-80 transition-opacity">
                                View all
                            </button>
                        </div>
                        
                        {/* The Grid 
                            - grid-cols-2 (mobile) / grid-cols-4 (desktop)
                            - gap increased to 4 for cleaner look
                            - auto-rows increased for better verticality
                        */}
                        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 auto-rows-[120px] md:auto-rows-[180px]">
                            {FEATURED_CATEGORIES.map((cat) => (
                                <div 
                                    key={cat.id}
                                    onClick={() => {
                                        setSelectedTopic(cat.id);
                                        window.scrollTo(0,0);
                                    }}
                                    className={`relative group cursor-pointer rounded-[24px] overflow-hidden bg-black transition-all duration-500 ease-[cubic-bezier(0.25,1,0.5,1)] hover:scale-[1.02] hover:shadow-2xl ring-1 ring-white/10 ${cat.className}`}
                                >
                                    {/* Image with slower, smoother zoom */}
                                    <img 
                                        src={cat.image} 
                                        alt={cat.label}
                                        className="absolute inset-0 w-full h-full object-cover transition-transform duration-1000 ease-out group-hover:scale-110 opacity-90"
                                    />
                                    
                                    {/* Gradient Overlay */}
                                    <div className="absolute inset-0 bg-gradient-to-t from-black/90 via-black/20 to-transparent opacity-80 transition-opacity duration-500 group-hover:opacity-90" />
                                    
                                    {/* Content Container */}
                                    <div className="absolute bottom-0 left-0 p-5 w-full">
                                        <div className="transform transition-transform duration-500 ease-[cubic-bezier(0.25,1,0.5,1)] group-hover:-translate-y-2">
                                            <span className="block text-white text-[20px] md:text-[24px] font-bold tracking-tighter leading-none mb-1 drop-shadow-md">
                                                {cat.label}
                                            </span>
                                            
                                            {/* Metadata that reveals/brightens on hover */}
                                            <span className="block text-[11px] font-medium text-white/70 uppercase tracking-widest transition-opacity duration-300 opacity-90 group-hover:opacity-100 group-hover:text-white/90">
                                                {TOPIC_METADATA[cat.id]?.stories || '10K+'} Stories
                                            </span>
                                        </div>

                                        {/* Floating Action Button-ish indicator that appears */}
                                        <div className="absolute right-4 bottom-4 w-10 h-10 rounded-full bg-white/20 backdrop-blur-md border border-white/10 flex items-center justify-center text-white opacity-0 translate-y-4 group-hover:opacity-100 group-hover:translate-y-0 transition-all duration-500 delay-75 shadow-lg">
                                             <span className="material-icons-round text-[20px]">arrow_forward</span>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </section>

                    {/* All Tags */}
                    <section className="animate-[fadeIn_0.6s_ease-out]">
                         <h3 className="text-[13px] font-bold text-zinc-400 uppercase tracking-widest mb-4">
                            All Topics
                        </h3>
                        <div className="flex flex-wrap gap-2">
                            {ALL_TOPICS.map((topic) => (
                                <button
                                    key={topic}
                                    onClick={() => {
                                        setSelectedTopic(topic);
                                        window.scrollTo(0,0);
                                    }}
                                    className="px-4 py-2.5 rounded-xl bg-zinc-100 dark:bg-zinc-900 border border-transparent hover:border-zinc-300 dark:hover:border-white/20 text-zinc-600 dark:text-zinc-300 font-semibold text-[14px] hover:text-black dark:hover:text-white transition-all active:scale-95"
                                >
                                    {topic}
                                </button>
                            ))}
                        </div>
                    </section>
                </div>
            </div>
        </div>
      );
  }

  // --- RENDER: TOPIC DETAIL ---
  return (
    <div className="flex flex-col h-full bg-white dark:bg-black font-sans relative animate-[fadeIn_0.3s_ease-out]">
      {/* Sticky Header */}
      <div className="sticky top-0 z-30 bg-white/95 dark:bg-black/95 backdrop-blur-xl border-b border-zinc-100 dark:border-white/10 transition-colors">
         <div className="px-5 py-3 flex items-center gap-4 overflow-x-auto hide-scrollbar">
            <button 
                onClick={() => setSelectedTopic(null)}
                className="flex-shrink-0 flex items-center gap-2 bg-zinc-100 dark:bg-zinc-900 px-4 py-2 rounded-full text-black dark:text-white hover:bg-zinc-200 dark:hover:bg-zinc-800 transition-colors group"
            >
                <span className="material-icons-round text-[20px] text-zinc-400 group-hover:text-black dark:text-zinc-400 dark:group-hover:text-white transition-colors">chevron_left</span>
                <span className="material-icons-round text-[20px] text-primary">explore</span>
                <span className="text-[13px] font-bold">Explorer</span>
            </button>
            <div className="w-px h-6 bg-zinc-200 dark:bg-white/10 flex-shrink-0"></div>
            {ALL_TOPICS.slice(0, 8).map((topic) => (
                <button 
                    key={topic} 
                    onClick={() => setSelectedTopic(topic)}
                    className={`flex-shrink-0 px-4 py-2 rounded-full text-[13px] font-medium transition-colors whitespace-nowrap ${
                        selectedTopic === topic 
                        ? 'text-black dark:text-white font-bold bg-white dark:bg-white/10 border border-zinc-200 dark:border-white/20 shadow-sm' 
                        : 'text-zinc-500 dark:text-zinc-400 bg-zinc-50 dark:bg-white/5 hover:bg-zinc-100 dark:hover:bg-white/10'
                    }`}
                >
                    {topic}
                </button>
            ))}
         </div>
      </div>

      <div className="flex-1 overflow-y-auto hide-scrollbar">
         {/* HEADER SECTION - Adapted to screenshot */}
         <div className="pt-16 pb-12 px-6 text-center border-b border-zinc-100 dark:border-white/10">
             
             {/* Avatar - Simple, clean circle without heavy borders */}
             <div className="w-20 h-20 rounded-full overflow-hidden mx-auto mb-5 shadow-sm">
                 <img 
                    src={FEATURED_CATEGORIES.find(c => c.id === selectedTopic)?.image || `https://ui-avatars.com/api/?name=${selectedTopic}&background=random`} 
                    className="w-full h-full object-cover"
                    alt={selectedTopic} 
                 />
             </div>

             {/* Title - Serif, Bold */}
             <h1 className="text-[40px] md:text-[48px] font-black text-black dark:text-white tracking-tight mb-3 font-serif animate-[fadeInUp_0.3s_ease-out] leading-tight">
                 {selectedTopic}
             </h1>
             
             {/* Metadata - Grey, Centered, Bullets */}
             <div className="flex items-center justify-center gap-2 text-[14px] text-zinc-500 dark:text-zinc-400 mb-6 font-sans">
                 <span>Topic</span>
                 <span className="text-[10px]">•</span>
                 <span>{currentTopicMeta.followers} followers</span>
                 <span className="text-[10px]">•</span>
                 <span>{currentTopicMeta.stories} stories</span>
             </div>

             {/* Follow Button - Black/White Pill */}
             <button 
                onClick={() => setIsFollowingTopic(!isFollowingTopic)}
                className={`px-8 py-2.5 rounded-full text-[14px] font-bold transition-all duration-200 active:scale-95 min-w-[120px] ${
                    isFollowingTopic 
                    ? 'bg-transparent border border-black dark:border-white text-black dark:text-white' 
                    : 'bg-black dark:bg-white text-white dark:text-black shadow-sm hover:opacity-80'
                }`}
             >
                 {isFollowingTopic ? 'Following' : 'Follow'}
             </button>
         </div>

         <div className="px-6 max-w-6xl mx-auto py-10">
            <div className="flex items-center gap-2 mb-6">
                <span className="material-icons-round text-yellow-500">star</span>
                <h2 className="text-[20px] font-bold text-black dark:text-white">Recommended stories</h2>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-x-12 gap-y-12 mb-16">
                {recommendedArticles.map(article => (
                    <ArticleCard 
                        key={`rec-${article.id}`} 
                        article={article} 
                        variant="grid" 
                        onClick={() => onArticleClick(article)}
                    />
                ))}
            </div>
            <div className="max-w-3xl">
                <div className="flex items-center justify-between mb-4 border-b border-zinc-100 dark:border-white/10 pb-4">
                     <h2 className="text-[20px] font-bold text-black dark:text-white">Latest</h2>
                     <div className="flex gap-2">
                        <button className="text-[13px] font-bold text-zinc-500 hover:text-black">Trending</button>
                        <button className="text-[13px] font-bold text-black dark:text-white underline">Newest</button>
                     </div>
                </div>
                <div>
                    {listArticles.map(article => (
                        <ArticleCard 
                            key={`list-${article.id}`}
                            article={article}
                            variant="standard"
                            onClick={() => onArticleClick(article)}
                        />
                    ))}
                </div>

                {/* Load More Button */}
                <div className="pt-8 pb-4 flex justify-center">
                    <button 
                        onClick={handleLoadMore}
                        disabled={isLoadingMore}
                        className="px-8 py-3 bg-white dark:bg-white/5 border border-zinc-200 dark:border-white/10 rounded-full text-[14px] font-bold text-zinc-600 dark:text-zinc-300 hover:bg-zinc-50 dark:hover:bg-white/10 transition-colors shadow-sm w-full md:w-auto active:scale-95 disabled:opacity-50 disabled:active:scale-100 flex items-center justify-center gap-2"
                    >
                        {isLoadingMore && <div className="w-4 h-4 border-2 border-zinc-400 border-t-transparent rounded-full animate-spin"></div>}
                        {isLoadingMore ? 'Loading stories...' : 'See more'}
                    </button>
                </div>
            </div>
         </div>
      </div>
    </div>
  );
};

export default ExploreView;