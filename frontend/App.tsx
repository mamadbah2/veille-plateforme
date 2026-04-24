import React, { useState, useEffect, useRef, useCallback, useMemo } from 'react';
import Header from './components/Header';
import ArticleCard from './components/ArticleCard';
import BottomNav from './components/BottomNav';
import Sidebar from './components/Sidebar';
import RightPanel from './components/RightPanel';
import SearchOverlay from './components/SearchOverlay';
import NotificationsOverlay, { NotificationItem } from './components/NotificationsOverlay';
import NotificationsFullView from './components/NotificationsFullView';
import BookmarksView from './components/BookmarksView';
import ProfileView from './components/ProfileView';
import ExploreView from './components/ExploreView';

import ArticleDetailView from './components/ArticleDetailView';
import SettingsOverlay from './components/SettingsOverlay';
import AiSummaryOverlay from './components/AiSummaryOverlay';
import TopicCustomizationOverlay from './components/TopicCustomizationOverlay';
import ArchiveView from './components/ArchiveView';
import Toast from './components/Toast';
import { Article, Tab, TextSize } from './types';

// Mock Data
const MOCK_ARTICLES: Article[] = [
  {
    id: '1',
    author: {
      name: 'Sarah Drasner',
      avatarUrl: 'https://i.pravatar.cc/150?u=sarah',
    },
    source: 'CSS Tricks',
    title: 'Modern CSS: Container Queries are finally here',
    excerpt: 'Say goodbye to media queries. Learn how to build truly modular components that respond to their parent container size.',
    thumbnailUrl: 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&q=80&w=800&h=600',
    date: '2h ago',
    readTime: '6 min',
    tags: ['CSS', 'Frontend', 'Design'],
    url: 'https://css-tricks.com',
  },
  {
    id: '2',
    author: {
      name: 'Dan Abramov',
      avatarUrl: 'https://i.pravatar.cc/150?u=dan',
    },
    source: 'React Blog',
    title: 'React 19: Everything you need to know about the compiler',
    excerpt: 'React 19 introduces automatic memoization via the new compiler. Here is how it changes your development workflow.',
    thumbnailUrl: 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?auto=format&fit=crop&q=80&w=300&h=300',
    date: '5h ago',
    readTime: '12 min',
    tags: ['React', 'JavaScript', 'Web'],
    url: 'https://react.dev/blog',
  },
  {
    id: '3',
    author: {
      name: 'Andrej Karpathy',
      avatarUrl: 'https://i.pravatar.cc/150?u=andrej',
    },
    source: 'AI Weekly',
    title: 'The State of LLMs: Gemini 1.5 Pro Analysis',
    excerpt: 'A deep dive into the 1M token context window and what it implies for RAG architectures in production.',
    thumbnailUrl: 'https://images.unsplash.com/photo-1620712943543-bcc4688e7485?auto=format&fit=crop&q=80&w=300&h=300',
    date: '1d ago',
    readTime: '15 min',
    tags: ['AI', 'LLM', 'Gemini'],
    url: 'https://blog.google/technology/ai/',
  },
  {
    id: '4',
    author: {
      name: 'Lee Robinson',
      avatarUrl: 'https://i.pravatar.cc/150?u=lee',
    },
    source: 'Vercel',
    title: 'Optimizing Next.js for Core Web Vitals',
    excerpt: 'Practical strategies to improve your LCP and CLS scores using the new Image component and font optimization.',
    thumbnailUrl: 'https://images.unsplash.com/photo-1550439062-609e1531270e?auto=format&fit=crop&q=80&w=300&h=300',
    date: '2d ago',
    readTime: '8 min',
    tags: ['Next.js', 'Performance'],
    url: 'https://vercel.com/blog',
  },
  {
    id: '5',
    author: {
      name: 'Linux Torvalds',
      avatarUrl: 'https://i.pravatar.cc/150?u=linus',
    },
    source: 'Kernel.org',
    title: 'Rust in the Linux Kernel: A Progress Report',
    excerpt: 'Examining the adoption rate of Rust drivers in the latest kernel release and the roadmap for memory safety.',
    thumbnailUrl: 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&q=80&w=300&h=300',
    date: '3d ago',
    readTime: '10 min',
    tags: ['Rust', 'Linux', 'Systems'],
    url: 'https://kernel.org',
  },
  {
    id: '6',
    author: {
      name: 'Jeff Atwood',
      avatarUrl: 'https://i.pravatar.cc/150?u=jeff',
    },
    source: 'Coding Horror',
    title: 'The Principles of Sustainable Software Engineering',
    excerpt: 'Sustainability is not just about energy efficiency; it is about building systems that are maintainable, scalable, and resilient.',
    thumbnailUrl: 'https://images.unsplash.com/photo-1461749280684-dccba630e2f6?auto=format&fit=crop&q=80&w=300&h=300',
    date: '4d ago',
    readTime: '9 min',
    tags: ['DevOps', 'Engineering'],
    url: 'https://blog.codinghorror.com',
  }
];

const DEFAULT_TABS = ['Latest', 'Frontend', 'Backend', 'AI/ML', 'DevOps', 'Mobile'];
const ALL_POSSIBLE_TAGS = Array.from(new Set(MOCK_ARTICLES.flatMap(a => a.tags)));

const MOCK_NOTIFICATIONS: NotificationItem[] = [
  {
    id: '1',
    type: 'alert',
    title: 'Breaking News',
    message: 'React 19 Release Candidate is now available for testing. Check out the new hooks!',
    time: '10 min ago',
    read: false,
  },
  {
    id: '2',
    type: 'info',
    title: 'Daily Digest',
    message: 'Your tech briefing for today is ready. You have 5 new articles matching your interests.',
    time: '2 hours ago',
    read: false,
  },
  {
    id: '3',
    type: 'success',
    title: 'Download Complete',
    message: 'The offline pack for "Rust Fundamentals" has been successfully saved to your device.',
    time: '5 hours ago',
    read: true,
  },
  {
    id: '4',
    type: 'info',
    title: 'New Follower',
    message: 'Sarah Drasner started following you.',
    time: 'Yesterday',
    read: true,
  },
  {
    id: '5',
    type: 'info',
    title: 'Article Trending',
    message: 'Your comment on "The State of LLMs" is getting a lot of attention.',
    time: 'Yesterday',
    read: true,
  }
];

const App: React.FC = () => {
  const [activeTab, setActiveTab] = useState<Tab>('Latest');
  const [tabs, setTabs] = useState<string[]>(DEFAULT_TABS);
  const [currentView, setCurrentView] = useState<'home' | 'search' | 'bookmarks' | 'profile' | 'notifications' | 'archive'>('home');
  const [savedArticleIds, setSavedArticleIds] = useState<Set<string>>(new Set());

  const [isLoadingFeed, setIsLoadingFeed] = useState(false);
  const [isViewLoading, setIsViewLoading] = useState(false);
  const [isLoadingMore, setIsLoadingMore] = useState(false);
  const [selectedArticle, setSelectedArticle] = useState<Article | null>(null);
  const [summaryArticle, setSummaryArticle] = useState<Article | null>(null);

  // Archive View State
  const [archiveTopic, setArchiveTopic] = useState<string>('Society');

  // Feed State
  const [displayArticles, setDisplayArticles] = useState<Article[]>([]);

  // App Settings State
  const [isDarkMode, setIsDarkMode] = useState(false);
  const [textSize, setTextSize] = useState<TextSize>('normal');
  const [isSettingsOpen, setIsSettingsOpen] = useState(false);

  // Notification State
  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const [notifications, setNotifications] = useState<NotificationItem[]>(MOCK_NOTIFICATIONS);

  // Search & Customize State
  const [isSearchOpen, setIsSearchOpen] = useState(false);
  const [isTopicCustomizerOpen, setIsTopicCustomizerOpen] = useState(false);

  // Toast State
  const [toast, setToast] = useState({ visible: false, message: '', icon: '' });

  // Pull to Refresh State
  const [pullY, setPullY] = useState(0);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const scrollContainerRef = useRef<HTMLDivElement>(null);
  const touchStartY = useRef(0);
  const isPulling = useRef(false);

  const unreadCount = notifications.filter(n => !n.read).length;

  // Dark Mode Effect
  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [isDarkMode]);

  // Initial Data Load & Filter Logic
  useEffect(() => {
    setIsLoadingFeed(true);

    // Simulate network delay
    const timer = setTimeout(() => {
      let filtered = MOCK_ARTICLES;
      if (activeTab !== 'Latest') {
        filtered = MOCK_ARTICLES.filter(article => {
          if (article.tags.includes(activeTab)) return true;
          if (activeTab === 'Frontend') return article.tags.some(t => ['React', 'CSS', 'Next.js', 'Web', 'Design'].includes(t));
          if (activeTab === 'Backend') return article.tags.some(t => ['Rust', 'Systems', 'Linux', 'Node', 'DB'].includes(t));
          if (activeTab === 'AI/ML') return article.tags.some(t => ['AI', 'LLM', 'Gemini'].includes(t));
          if (activeTab === 'DevOps') return article.tags.some(t => ['DevOps', 'Engineering', 'Systems'].includes(t));
          if (activeTab === 'Mobile') return article.tags.some(t => ['iOS', 'Android', 'React Native'].includes(t));
          return false;
        });
      }
      setDisplayArticles(filtered);
      setIsLoadingFeed(false);
    }, 600);

    return () => clearTimeout(timer);
  }, [activeTab]);


  const showToast = (message: string, icon: string = 'check_circle') => {
    setToast({ visible: true, message, icon });
  };

  const handleToggleTextSize = () => {
    setTextSize(prev => {
      if (prev === 'normal') return 'large';
      if (prev === 'large') return 'extra';
      return 'normal';
    });
    showToast('Text size updated', 'text_fields');
  };

  const handleMarkAllNotificationsRead = () => {
    setNotifications(prev => prev.map(n => ({ ...n, read: true })));
    showToast('All notifications read');
  };

  const handleMarkSingleNotificationRead = (id: string) => {
    setNotifications(prev => prev.map(n => n.id === id ? { ...n, read: true } : n));
  };

  const handleDeleteNotification = (id: string) => {
    setNotifications(prev => prev.filter(n => n.id !== id));
    showToast('Notification deleted', 'delete');
  };

  const handleNotificationAction = (id: string, action: string) => {
    if (action === 'view') {
      handleMarkSingleNotificationRead(id);
      setIsNotificationsOpen(false);
      showToast('Opening content...', 'open_in_new');
    }
  };

  const toggleBookmark = (id: string) => {
    setSavedArticleIds(prev => {
      const next = new Set(prev);
      const isAdding = !next.has(id);

      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }

      if (isAdding) {
        showToast('Saved to Library', 'bookmark');
      } else {
        showToast('Removed from Library', 'delete');
      }

      return next;
    });
  };

  const handleTabChange = (tab: Tab) => {
    if (tab === activeTab) return;
    setActiveTab(tab);
  };

  const handleUpdateTabs = (newTabs: string[]) => {
    setTabs(newTabs);
    if (!newTabs.includes(activeTab)) {
      setActiveTab('Latest');
    }
    showToast('Topics updated', 'tune');
  };

  const handleLoadMore = () => {
    if (isLoadingMore) return;
    setIsLoadingMore(true);
    // Simulate loading more data by appending existing articles with new IDs
    setTimeout(() => {
      const newArticles = MOCK_ARTICLES.slice(0, 3).map(a => ({
        ...a,
        id: `${a.id}-${Date.now()}-${Math.random()}`
      }));
      setDisplayArticles(prev => [...prev, ...newArticles]);
      setIsLoadingMore(false);
    }, 1500);
  };

  const handleTouchStart = (e: React.TouchEvent) => {
    if (scrollContainerRef.current && scrollContainerRef.current.scrollTop === 0) {
      touchStartY.current = e.touches[0].clientY;
      isPulling.current = true;
    }
  };

  const handleTouchMove = (e: React.TouchEvent) => {
    if (!isPulling.current) return;

    const currentY = e.touches[0].clientY;
    const diff = currentY - touchStartY.current;

    if (diff > 0 && scrollContainerRef.current?.scrollTop === 0) {
      const dampenedDiff = Math.min(diff * 0.4, 150);
      setPullY(dampenedDiff);
      if (diff < 200) e.preventDefault();
    } else {
      setPullY(0);
    }
  };

  const handleTouchEnd = () => {
    isPulling.current = false;
    if (pullY > 60) {
      setIsRefreshing(true);
      setPullY(60);
      setTimeout(() => {
        setIsRefreshing(false);
        setPullY(0);
        showToast('Feed Updated', 'refresh');
      }, 1500);
    } else {
      setPullY(0);
    }
  };

  const handleViewNavigation = (view: 'home' | 'search' | 'bookmarks' | 'profile' | 'notifications' | 'archive') => {
    // Close settings if open when navigating via sidebar
    setIsSettingsOpen(false);

    if (view === currentView) return;

    if (view !== 'home' && view !== 'archive') { // Don't show loading for immediate transitions
      setIsViewLoading(true);
      setTimeout(() => {
        setIsViewLoading(false);
      }, 800);
    }

    setCurrentView(view);
  };

  const handleAiSummaryClick = (article: Article) => {
    setSummaryArticle(article);
  };

  const handleReadFullFromSummary = () => {
    if (summaryArticle) {
      setSelectedArticle(summaryArticle);
      setSummaryArticle(null);
    }
  };

  const handleOpenArchive = (topic: string) => {
    setArchiveTopic(topic);
    handleViewNavigation('archive');
  };

  const savedArticles = MOCK_ARTICLES.filter(article => savedArticleIds.has(article.id));
  const lastSavedArticle = savedArticles.length > 0 ? savedArticles[savedArticles.length - 1] : null;

  const allTopics = useMemo(() => {
    const tags = new Set([...DEFAULT_TABS, ...ALL_POSSIBLE_TAGS]);
    return Array.from(tags).sort();
  }, []);

  // --- RENDERING HELPERS ---
  const renderHomeFeed = () => {
    if (isLoadingFeed) {
      return (
        <div className="mt-2 pb-28 animate-fadeInUp max-w-4xl mx-auto w-full">
          <ArticleCard article={MOCK_ARTICLES[0]} loading={true} variant="featured" />
          <ArticleCard article={MOCK_ARTICLES[0]} loading={true} />
          <ArticleCard article={MOCK_ARTICLES[0]} loading={true} />
        </div>
      );
    }

    if (displayArticles.length === 0) {
      return (
        <div className="py-20 text-center opacity-60">
          <div className="w-16 h-16 bg-slate-100 dark:bg-white/5 rounded-full flex items-center justify-center mx-auto mb-4">
            <span className="material-icons-round text-slate-400 text-[24px]">newspaper</span>
          </div>
          <p className="text-slate-500">No articles found for "{activeTab}"</p>
        </div>
      );
    }

    // Should we show the featured article? Only if on 'Latest' tab
    const hasFeatured = activeTab === 'Latest';
    const featuredArticle = hasFeatured ? displayArticles[0] : null;
    const listArticles = hasFeatured ? displayArticles.slice(1) : displayArticles;

    return (
      <div key={activeTab} className="mt-2 pb-28 animate-fadeInUp max-w-4xl mx-auto w-full">

        {/* Featured Article */}
        {hasFeatured && featuredArticle && (
          <ArticleCard
            article={featuredArticle}
            variant="featured"
            isBookmarked={savedArticleIds.has(featuredArticle.id)}
            onToggleBookmark={() => toggleBookmark(featuredArticle.id)}
            onClick={() => setSelectedArticle(featuredArticle)}
            onAiSummaryClick={() => handleAiSummaryClick(featuredArticle)}
          />
        )}

        {/* Section Header: Latest / Trending */}
        <div className="flex items-center justify-between px-5 mb-6 mt-2">
          <h2 className="text-[20px] font-bold text-slate-900 dark:text-white">
            {hasFeatured ? 'Latest' : 'Stories'}
          </h2>
          <div className="flex gap-4 text-[13px] font-medium">
            <button className="text-slate-400 hover:text-slate-600 dark:hover:text-slate-300 transition-colors">Trending</button>
            <button className="text-slate-900 dark:text-white underline decoration-2 underline-offset-4 decoration-primary">Newest</button>
          </div>
        </div>

        {/* List */}
        {listArticles.map((article) => (
          <ArticleCard
            key={article.id}
            article={article}
            variant="standard"
            isBookmarked={savedArticleIds.has(article.id)}
            onToggleBookmark={() => toggleBookmark(article.id)}
            onClick={() => setSelectedArticle(article)}
            onAiSummaryClick={() => handleAiSummaryClick(article)}
          />
        ))}

        {/* Load More Button */}
        <div className="px-5 py-8 flex justify-center">
          <button
            onClick={handleLoadMore}
            disabled={isLoadingMore}
            className="px-8 py-3 bg-white dark:bg-white/5 border border-slate-200 dark:border-white/10 rounded-full text-[14px] font-bold text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-white/10 transition-colors shadow-sm w-full md:w-auto active:scale-95 disabled:opacity-50 disabled:active:scale-100 flex items-center justify-center gap-2"
          >
            {isLoadingMore && <div className="w-4 h-4 border-2 border-slate-400 border-t-transparent rounded-full animate-spin"></div>}
            {isLoadingMore ? 'Loading stories...' : 'See more'}
          </button>
        </div>
      </div>
    );
  };

  return (
    <div className="flex min-h-screen bg-[#F2F2F7] dark:bg-black font-sans transition-colors duration-300">

      <Toast
        message={toast.message}
        isVisible={toast.visible}
        icon={toast.icon}
        onHide={() => setToast(prev => ({ ...prev, visible: false }))}
      />



      {/* --- DESKTOP: LEFT SIDEBAR --- */}
      <Sidebar
        currentView={currentView === 'archive' ? 'search' : currentView} // Keep 'Explorer' highlighted for Archive view
        onNavigate={handleViewNavigation}
        onOpenSettings={() => setIsSettingsOpen(true)}
      />

      {/* --- MAIN CONTENT AREA --- */}
      <div className="flex-1 w-full bg-white dark:bg-black md:border-r border-slate-200 dark:border-white/10 flex flex-col relative h-screen overflow-hidden">

        {/* --- VIEW: HOME --- */}
        {currentView === 'home' && (
          <>
            {/* Header */}
            <Header
              activeTab={activeTab}
              tabs={tabs}
              onTabChange={handleTabChange}
              onOpenNotifications={() => setIsNotificationsOpen(!isNotificationsOpen)}
              hasUnreadNotifications={unreadCount > 0}
              onSearchClick={() => setIsSearchOpen(true)}
              onCustomizeTabs={() => setIsTopicCustomizerOpen(true)}
            />

            {/* NOTIFICATIONS POPUP */}
            <NotificationsOverlay
              isOpen={isNotificationsOpen}
              onClose={() => setIsNotificationsOpen(false)}
              notifications={notifications}
              onMarkAllRead={handleMarkAllNotificationsRead}
              onMarkAsRead={handleMarkSingleNotificationRead}
              onAction={handleNotificationAction}
              onViewAll={() => handleViewNavigation('notifications')}
            />

            {/* Refresh Indicator */}
            <div
              className="absolute left-0 right-0 z-10 flex justify-center pointer-events-none transition-transform duration-300"
              style={{
                top: '110px',
                transform: `translateY(${isRefreshing ? 20 : pullY * 0.5}px)`,
                opacity: pullY > 10 || isRefreshing ? 1 : 0
              }}
            >
              <div className="w-8 h-8 rounded-full bg-white dark:bg-slate-800 shadow-md flex items-center justify-center">
                {isRefreshing ? (
                  <div className="w-4 h-4 border-2 border-slate-300 border-t-primary rounded-full animate-spin"></div>
                ) : (
                  <span
                    className="material-icons-round text-primary text-[18px] transition-transform"
                    style={{ transform: `rotate(${pullY * 2}deg)` }}
                  >
                    refresh
                  </span>
                )}
              </div>
            </div>

            {/* Feed */}
            <main
              ref={scrollContainerRef}
              className="flex-1 overflow-y-auto hide-scrollbar bg-white dark:bg-black relative transition-transform duration-300 ease-out"
              style={{ transform: `translateY(${pullY}px)` }}
              onTouchStart={handleTouchStart}
              onTouchMove={handleTouchMove}
              onTouchEnd={handleTouchEnd}
            >
              {renderHomeFeed()}
            </main>
          </>
        )}

        {/* --- VIEW: EXPLORE --- */}
        {currentView === 'search' && (
          <ExploreView
            articles={MOCK_ARTICLES}
            onArticleClick={setSelectedArticle}
            onOpenSearch={() => setIsSearchOpen(true)}
            onSeeMore={handleOpenArchive}
          />
        )}

        {/* --- VIEW: ARCHIVE (New) --- */}
        {currentView === 'archive' && (
          <ArchiveView
            topic={archiveTopic}
            articles={MOCK_ARTICLES}
            onBack={() => setCurrentView('search')}
            onArticleClick={setSelectedArticle}
            onToggleBookmark={toggleBookmark}
            savedArticleIds={savedArticleIds}
          />
        )}

        {/* --- VIEW: BOOKMARKS --- */}
        {currentView === 'bookmarks' && (
          <BookmarksView
            articles={savedArticles}
            onToggleBookmark={toggleBookmark}
            onNavigateHome={() => handleViewNavigation('home')}
            onArticleClick={setSelectedArticle}
            isLoading={isViewLoading}
          />
        )}

        {/* --- VIEW: PROFILE --- */}
        {currentView === 'profile' && (
          <ProfileView
            isLoading={isViewLoading}
            isDarkMode={isDarkMode}
            onToggleDarkMode={() => {
              setIsDarkMode(!isDarkMode);
              showToast(isDarkMode ? 'Light Mode Enabled' : 'Dark Mode Enabled', 'contrast');
            }}
            textSize={textSize}
            onToggleTextSize={handleToggleTextSize}
            onOpenSettings={() => setIsSettingsOpen(true)}
            savedCount={savedArticleIds.size}
            lastSavedArticle={lastSavedArticle}
            onNavigateToLibrary={() => handleViewNavigation('bookmarks')}
          />
        )}

        {/* --- VIEW: NOTIFICATIONS (Full Page) --- */}
        {currentView === 'notifications' && (
          <NotificationsFullView
            notifications={notifications}
            onMarkAsRead={handleMarkSingleNotificationRead}
            onMarkAllRead={handleMarkAllNotificationsRead}
            onDelete={handleDeleteNotification}
          />
        )}

        {/* Global Search Overlay */}
        <SearchOverlay
          isOpen={isSearchOpen}
          onClose={() => setIsSearchOpen(false)}
          articles={MOCK_ARTICLES}
          onArticleClick={(article) => {
            setSelectedArticle(article);
            setIsSearchOpen(false);
          }}
        />

        {/* Mobile Gradient Blur */}
        {!isSearchOpen && !selectedArticle && (
          <div className="md:hidden absolute bottom-0 left-0 right-0 h-24 pointer-events-none z-30 bg-gradient-to-t from-white/80 via-white/40 to-transparent dark:from-black/80 dark:via-black/40 dark:to-transparent" />
        )}

        {/* Mobile Bottom Navigation */}
        <BottomNav
          currentView={currentView === 'archive' ? 'search' : currentView}
          onNavigate={handleViewNavigation}
          isSearchOverlayOpen={isSearchOpen}
          onCloseSearchOverlay={() => setIsSearchOpen(false)}
          isHidden={!!selectedArticle}
        />

        {/* Mobile Home Indicator */}
        <div className="md:hidden absolute bottom-2 left-1/2 transform -translate-x-1/2 w-36 h-1 bg-black/20 dark:bg-white/20 rounded-full z-[100] pointer-events-none"></div>

      </div>

      {/* --- DESKTOP: RIGHT PANEL --- */}
      {/* Hidden when viewing profile to allow profile sidebar to take space */}
      {currentView !== 'profile' && (
        <RightPanel onSearch={() => setIsSearchOpen(true)} />
      )}

      {/* --- OVERLAYS --- */}

      {selectedArticle && (
        <ArticleDetailView
          article={selectedArticle}
          allArticles={MOCK_ARTICLES}
          onClose={() => setSelectedArticle(null)}
          isBookmarked={savedArticleIds.has(selectedArticle.id)}
          onToggleBookmark={() => toggleBookmark(selectedArticle.id)}
          textSize={textSize}
          onShowToast={showToast}
          onArticleClick={setSelectedArticle}
        />
      )}

      {/* AI Summary Overlay */}
      {summaryArticle && (
        <AiSummaryOverlay
          article={summaryArticle}
          isOpen={!!summaryArticle}
          onClose={() => setSummaryArticle(null)}
          onReadFullArticle={handleReadFullFromSummary}
        />
      )}

      <SettingsOverlay
        isOpen={isSettingsOpen}
        onClose={() => setIsSettingsOpen(false)}
      />

      <TopicCustomizationOverlay
        isOpen={isTopicCustomizerOpen}
        onClose={() => setIsTopicCustomizerOpen(false)}
        activeTabs={tabs}
        allAvailableTopics={allTopics}
        onSave={handleUpdateTabs}
      />

    </div>
  );
};

export default App;