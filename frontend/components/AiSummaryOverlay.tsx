import React, { useState, useEffect, useRef } from 'react';
import { Article } from '../types';
import { GoogleGenAI } from "@google/genai";

interface AiSummaryOverlayProps {
  article: Article;
  isOpen: boolean;
  onClose: () => void;
  onReadFullArticle: () => void;
}

const AiSummaryOverlay: React.FC<AiSummaryOverlayProps> = ({ article, isOpen, onClose, onReadFullArticle }) => {
  const [isVisible, setIsVisible] = useState(false);
  const [summaryText, setSummaryText] = useState("");
  const [isGenerating, setIsGenerating] = useState(false);
  const abortControllerRef = useRef<AbortController | null>(null);

  useEffect(() => {
    if (isOpen) {
      setIsVisible(true);
      setSummaryText("");
      generateSummary();
      // Prevent body scrolling
      document.body.style.overflow = 'hidden';
    } else {
      const timer = setTimeout(() => setIsVisible(false), 300);
      // Abort any ongoing generation when closing
      if (abortControllerRef.current) {
        abortControllerRef.current.abort();
      }
      document.body.style.overflow = 'unset';
      return () => clearTimeout(timer);
    }
  }, [isOpen, article]);

  const generateSummary = async () => {
    setIsGenerating(true);
    abortControllerRef.current = new AbortController();

    try {
      // Initialize Gemini API
      // Note: Assuming process.env.API_KEY is available as per instructions
      const ai = new GoogleGenAI({ apiKey: process.env.API_KEY });
      
      const prompt = `
        Summarize the following tech article in 2-3 paragraphs.
        Make it sound like a high-quality editorial summary.
        Tone: Professional, Insightful.
        
        Title: ${article.title}
        Topic: ${article.tags.join(', ')}
        Content Context: ${article.excerpt}
      `;

      const response = await ai.models.generateContentStream({
        model: 'gemini-3-flash-preview',
        contents: prompt,
        config: {
          systemInstruction: "You are an expert editor. Summarize this for a senior developer.",
        }
      });

      let fullText = "";
      for await (const chunk of response) {
        if (chunk.text) {
          fullText += chunk.text;
          setSummaryText(fullText); // Stream updates to UI
        }
      }
    } catch (error) {
      console.error("Gemini generation failed", error);
      setSummaryText("Unable to generate summary at this time. Please check your connection and try again.");
    } finally {
      setIsGenerating(false);
    }
  };

  const handleOpenSource = () => {
    if (article.url) {
        window.open(article.url, '_blank', 'noopener,noreferrer');
    }
  };

  const renderContent = () => {
    if (isGenerating && !summaryText) {
        return (
            <div className="space-y-4 animate-pulse pt-2">
                <div className="h-4 bg-slate-100 dark:bg-white/10 rounded w-full"></div>
                <div className="h-4 bg-slate-100 dark:bg-white/10 rounded w-[92%]"></div>
                <div className="h-4 bg-slate-100 dark:bg-white/10 rounded w-[96%]"></div>
                <div className="h-4 bg-slate-100 dark:bg-white/10 rounded w-[80%]"></div>
                <div className="h-4 bg-transparent w-full my-4"></div>
                <div className="h-4 bg-slate-100 dark:bg-white/10 rounded w-full"></div>
                <div className="h-4 bg-slate-100 dark:bg-white/10 rounded w-[90%]"></div>
            </div>
        );
    }

    // Simple markdown-ish parser for bold text (**text**)
    const parts = summaryText.split(/(\*\*.*?\*\*)/g);
    
    return (
        <div className="font-serif text-[18px] md:text-[20px] leading-[1.8] text-slate-800 dark:text-slate-200 animate-[fadeIn_0.5s_ease-out]">
            {summaryText.split('\n').map((paragraph, idx) => {
                if (!paragraph.trim()) return <br key={idx} />;
                
                // Bold parsing inside paragraph
                const paraParts = paragraph.split(/(\*\*.*?\*\*)/g);
                return (
                    <p key={idx} className="mb-6 first-letter:float-left first-letter:text-[3.5em] first-letter:pr-3 first-letter:font-black first-letter:leading-[0.8] first-letter:text-slate-900 dark:first-letter:text-white first:first-letter:block">
                        {paraParts.map((part, i) => {
                            if (part.startsWith('**') && part.endsWith('**')) {
                                return <strong key={i} className="font-bold text-slate-900 dark:text-white">{part.slice(2, -2)}</strong>;
                            }
                            return <span key={i}>{part}</span>;
                        })}
                    </p>
                );
            })}
            {isGenerating && <span className="inline-block w-2 h-5 bg-primary ml-1 animate-pulse align-middle"></span>}
        </div>
    );
  };

  if (!isOpen && !isVisible) return null;

  return (
    <div className={`fixed inset-0 z-[110] flex items-end md:items-center justify-center p-0 md:p-6 transition-all duration-300 ${isOpen ? 'opacity-100' : 'opacity-0'}`}>
      
      {/* Backdrop */}
      <div 
        className="absolute inset-0 bg-black/60 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* Card Modal */}
      <div className={`
          relative w-full md:max-w-2xl bg-white dark:bg-[#1C1C1E] rounded-t-[32px] md:rounded-[32px] shadow-2xl overflow-hidden flex flex-col max-h-[90vh] md:max-h-[85vh]
          transition-all duration-500 cubic-bezier(0.2, 0.8, 0.2, 1)
          ${isOpen ? 'translate-y-0 scale-100' : 'translate-y-full md:translate-y-12 md:scale-95'}
      `}>
          
          {/* Header */}
          <div className="flex items-center justify-between px-6 py-5 border-b border-slate-100 dark:border-white/5 sticky top-0 bg-white/90 dark:bg-[#1C1C1E]/90 backdrop-blur-md z-20">
             <button 
                onClick={onClose}
                className="w-9 h-9 rounded-full bg-slate-900 dark:bg-white text-white dark:text-black flex items-center justify-center hover:opacity-80 transition-opacity"
             >
                 <span className="material-icons-round text-[18px]">close</span>
             </button>

             <div className="flex items-center gap-2">
                 <div className="w-9 h-9 rounded-full bg-primary flex items-center justify-center text-white shadow-lg shadow-primary/30">
                     <span className="material-icons-round text-[18px]">auto_awesome</span>
                 </div>
             </div>
          </div>

          {/* Content Scroll Area */}
          <div className="flex-1 overflow-y-auto px-6 md:px-10 py-6">
              
              {/* Author Row */}
              <div className="flex items-center justify-between mb-6">
                   <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-full bg-slate-200 dark:bg-white/10 overflow-hidden">
                            <img src={article.author.avatarUrl} alt="" className="w-full h-full object-cover"/>
                        </div>
                        <div>
                            <p className="text-[14px] font-bold text-slate-900 dark:text-white">{article.author.name}</p>
                            <p className="text-[12px] text-slate-500 font-medium">{article.date}</p>
                        </div>
                   </div>
                   
                   {/* External Link Button in Header */}
                   <button 
                        onClick={handleOpenSource}
                        className="p-2 rounded-full bg-slate-100 dark:bg-white/10 text-slate-500 dark:text-slate-300 hover:text-primary dark:hover:text-white transition-colors"
                        title="View Original"
                   >
                        <span className="material-icons-round text-[20px]">public</span>
                   </button>
              </div>

              {/* Title */}
              <h2 className="text-[26px] md:text-[32px] font-black text-slate-900 dark:text-white leading-[1.15] mb-8 tracking-tight font-display">
                  {article.title}
              </h2>

              {/* AI Generated Body */}
              <div className="mb-6">
                  {renderContent()}
              </div>

          </div>

          {/* Footer Actions */}
          <div className="p-4 md:p-6 border-t border-slate-100 dark:border-white/5 bg-slate-50 dark:bg-[#252527] flex justify-between items-center z-20">
              <div className="flex gap-6 pl-2">
                  <button className="flex flex-col items-center gap-0.5 group">
                      <span className="material-icons-round text-[22px] text-slate-400 group-hover:text-slate-900 dark:group-hover:text-white transition-colors">ios_share</span>
                      <span className="text-[10px] font-bold text-slate-400">Share</span>
                  </button>
              </div>

              <button 
                onClick={onReadFullArticle}
                className="bg-slate-900 dark:bg-white text-white dark:text-black px-6 py-3 rounded-full font-bold text-[13px] hover:opacity-90 transition-opacity shadow-lg"
              >
                 Read Full Article
              </button>
          </div>
      </div>
    </div>
  );
};

export default AiSummaryOverlay;