import React from 'react';
import { Article } from '../types';
import ArticleCard from './ArticleCard';

interface ArchiveViewProps {
  topic: string;
  articles: Article[];
  onBack: () => void;
  onArticleClick: (article: Article) => void;
  onToggleBookmark?: (id: string) => void;
  savedArticleIds?: Set<string>;
}

const ArchiveView: React.FC<ArchiveViewProps> = ({ 
    topic, 
    articles, 
    onBack, 
    onArticleClick,
    onToggleBookmark, 
    savedArticleIds
}) => {
  
  return (
    <div className="flex flex-col h-full bg-white dark:bg-black font-sans animate-fadeIn">
        
        {/* Sticky Header / Breadcrumbs */}
        <div className="sticky top-0 z-20 bg-white/95 dark:bg-black/95 backdrop-blur-xl border-b border-transparent">
             <div className="max-w-3xl mx-auto px-6 pt-8 pb-4">
                 <div className="flex items-center gap-2 text-[14px] text-zinc-500">
                     <button onClick={onBack} className="hover:text-black dark:hover:text-white transition-colors">
                        {topic}
                     </button>
                     <span className="text-zinc-300 text-[10px]">example</span> {/* Chevron replacement */}
                     <span className="text-black dark:text-white font-medium">Archive</span>
                 </div>
             </div>
        </div>

        <div className="flex-1 overflow-y-auto hide-scrollbar">
            <div className="max-w-3xl mx-auto pb-24">
                
                {/* Title Section */}
                <div className="px-6 pt-8 pb-10 border-b border-zinc-100 dark:border-white/5">
                    <h1 className="text-[38px] md:text-[46px] font-black text-black dark:text-white tracking-tighter leading-tight mb-10">
                        Archive of stories in "{topic}"
                    </h1>

                    {/* Filters */}
                    <div className="flex flex-wrap gap-3">
                        <FilterButton label="Latest" />
                        <FilterButton label="All years" />
                        <FilterButton label="All months" />
                    </div>
                </div>

                {/* Article List - Using shared ArticleCard */}
                <div className="mt-2">
                    {articles.map(article => (
                        <ArticleCard 
                            key={article.id} 
                            article={article}
                            variant="standard"
                            onClick={() => onArticleClick(article)}
                            isBookmarked={savedArticleIds?.has(article.id)}
                            onToggleBookmark={() => onToggleBookmark && onToggleBookmark(article.id)}
                        />
                    ))}
                    {/* Duplicate list to simulate more content */}
                    {articles.map(article => (
                        <ArticleCard 
                            key={`${article.id}-dup`} 
                            article={{...article, id: `${article.id}-dup`}}
                            variant="standard"
                            onClick={() => onArticleClick(article)}
                            isBookmarked={savedArticleIds?.has(article.id)}
                            onToggleBookmark={() => onToggleBookmark && onToggleBookmark(article.id)}
                        />
                    ))}
                </div>

            </div>
        </div>
    </div>
  );
};

const FilterButton = ({ label }: { label: string }) => (
    <button className="flex items-center gap-2 px-4 py-2 rounded-full border border-zinc-200 dark:border-white/10 text-[13px] font-medium text-zinc-700 dark:text-zinc-300 hover:border-primary/50 dark:hover:border-primary/50 hover:bg-zinc-50 dark:hover:bg-white/5 transition-all">
        {label}
        <span className="material-icons-round text-[16px] text-zinc-400">keyboard_arrow_down</span>
    </button>
);

export default ArchiveView;