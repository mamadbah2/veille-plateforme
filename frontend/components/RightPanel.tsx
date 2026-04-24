import React from 'react';
import { Article } from '../types';

// Using mock data from App.tsx concept, simplified for the UI
const TRENDING_TOPICS = ['React 19', 'Tailwind v4', 'AI Agents', 'Bun vs Node', 'CSS Anchoring', 'Rust', 'WebAssembly', 'System Design'];

const TRENDING_ARTICLES = [
    {
        id: 't1',
        rank: '01',
        source: 'React Blog',
        authorAvatar: 'https://i.pravatar.cc/150?u=dan',
        title: 'React 19: Everything you need to know about the compiler',
        meta: '12 min • React'
    },
    {
        id: 't2',
        rank: '02',
        source: 'AI Weekly',
        authorAvatar: 'https://i.pravatar.cc/150?u=andrej',
        title: 'The State of LLMs: Gemini 1.5 Pro Analysis',
        meta: '15 min • AI'
    },
    {
        id: 't3',
        rank: '03',
        source: 'Vercel',
        authorAvatar: 'https://i.pravatar.cc/150?u=lee',
        title: 'Optimizing Next.js for Core Web Vitals',
        meta: '8 min • Next.js'
    },
    {
        id: 't4',
        rank: '04',
        source: 'Kernel.org',
        authorAvatar: 'https://i.pravatar.cc/150?u=linus',
        title: 'Rust in the Linux Kernel: A Progress Report',
        meta: '10 min • Rust'
    }
];

interface RightPanelProps {
    onSearch: (term: string) => void;
}

const RightPanel: React.FC<RightPanelProps> = ({ onSearch }) => {
  return (
    <aside className="hidden lg:flex flex-col w-[360px] h-screen sticky top-0 border-l border-slate-100 dark:border-white/5 bg-white dark:bg-black overflow-y-auto hide-scrollbar z-10">
       
       <div className="px-6 py-8">
            {/* Recommended Topics (Pills) */}
            <div className="mb-10">
                <h3 className="text-[15px] font-bold text-slate-900 dark:text-white mb-4 tracking-tight">Recommended topics</h3>
                <div className="flex flex-wrap gap-2">
                    {TRENDING_TOPICS.map((tag) => (
                        <button 
                            key={tag} 
                            className="px-3.5 py-1.5 bg-slate-100 dark:bg-white/5 rounded-full text-[12px] text-slate-700 dark:text-slate-300 font-semibold hover:bg-slate-200 dark:hover:bg-white/10 transition-colors"
                            onClick={() => onSearch(tag)}
                        >
                            {tag}
                        </button>
                    ))}
                </div>
            </div>

            {/* Trending on DevPulse (Numbered List) */}
            <div>
                <div className="flex items-center gap-2 mb-6">
                    <span className="material-icons-outlined text-[20px] text-slate-900 dark:text-white">trending_up</span>
                    <h3 className="text-[12px] font-black text-slate-900 dark:text-white uppercase tracking-widest">
                        TRENDING ON DEVPULSE
                    </h3>
                </div>

                <div className="space-y-7">
                    {TRENDING_ARTICLES.map((item) => (
                        <div key={item.id} className="flex gap-4 group cursor-pointer">
                            {/* Number */}
                            <span className="text-[32px] font-bold text-slate-200 dark:text-white/20 leading-none -mt-1 font-display">
                                {item.rank}
                            </span>
                            
                            {/* Content */}
                            <div className="flex-1">
                                {/* Author/Source Line */}
                                <div className="flex items-center gap-2 mb-1.5">
                                    <img src={item.authorAvatar} alt="" className="w-5 h-5 rounded-full object-cover" />
                                    <span className="text-[13px] font-bold text-slate-700 dark:text-slate-200">
                                        {item.source}
                                    </span>
                                </div>
                                
                                {/* Title */}
                                <h4 className="text-[16px] font-bold text-slate-900 dark:text-white leading-snug mb-1.5 group-hover:text-primary transition-colors line-clamp-2">
                                    {item.title}
                                </h4>
                                
                                {/* Meta */}
                                <span className="text-[13px] font-medium text-slate-400">
                                    {item.meta}
                                </span>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            {/* Footer Links (Compact) */}
            <div className="mt-12 pt-6 border-t border-slate-100 dark:border-white/5 flex flex-wrap gap-x-4 gap-y-2 text-[11px] text-slate-400 font-medium">
                <a href="#" className="hover:text-slate-600 dark:hover:text-slate-300">Help</a>
                <a href="#" className="hover:text-slate-600 dark:hover:text-slate-300">Status</a>
                <a href="#" className="hover:text-slate-600 dark:hover:text-slate-300">Privacy</a>
                <a href="#" className="hover:text-slate-600 dark:hover:text-slate-300">Terms</a>
                <span className="text-slate-300 dark:text-slate-600">© 2024</span>
            </div>
       </div>
    </aside>
  );
};

export default RightPanel;