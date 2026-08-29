import { Outlet, Navigate, Link, useLocation } from 'react-router-dom';
import { Activity, Home, User, FileText, MessageSquare, Settings, LogOut, Menu, Loader2, Stethoscope, Apple, Dumbbell, Moon, Accessibility, Sparkles, X, Bell } from 'lucide-react';
import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { motion, AnimatePresence } from 'framer-motion';
import { notificationService, NotificationDto } from '../services/notification';

const DashboardLayout = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [notificationsOpen, setNotificationsOpen] = useState(false);
  const [notifications, setNotifications] = useState<NotificationDto[]>([]);
  const location = useLocation();
  const { user, isAuthenticated, isLoading, logout } = useAuth();

  useEffect(() => {
    setSidebarOpen(false);
  }, [location.pathname]);

  useEffect(() => {
    if (isAuthenticated) {
      notificationService.getUserNotifications()
        .then(data => setNotifications(data))
        .catch(err => console.error('Failed to load notifications', err));
    }
  }, [isAuthenticated]);

  const markAsRead = async (id: number) => {
    try {
      await notificationService.markAsRead(id);
      setNotifications(prev => prev.map(n => n.id === id ? { ...n, read: true } : n));
    } catch (err) {
      console.error('Failed to mark as read', err);
    }
  };

  const unreadCount = notifications.filter(n => !n.read).length;

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-50">
        <Loader2 className="h-8 w-8 text-primary-600 animate-spin" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  const navigationGroups = [
    {
      title: 'MAIN',
      items: [
        { name: 'Dashboard', href: '/dashboard', icon: Home },
      ]
    },
    {
      title: 'HEALTH',
      items: [
        { name: 'Health Profile', href: '/profile', icon: User },
        { name: 'Digital Twin', href: '/twin', icon: Accessibility },
        { name: 'Medical Reports', href: '/reports', icon: FileText },
        { name: 'Symptom Analyzer', href: '/symptoms', icon: Stethoscope },
        { name: 'AI Chat', href: '/chat', icon: MessageSquare },
      ]
    },
    {
      title: 'WELLNESS',
      items: [
        { name: 'Nutrition', href: '/nutrition', icon: Apple },
        { name: 'Fitness', href: '/fitness', icon: Dumbbell },
        { name: 'Wellness', href: '/wellness', icon: Moon },
      ]
    },
    {
      title: 'SYSTEM',
      items: [
        { name: 'Settings', href: '/settings', icon: Settings },
      ]
    }
  ];

  const SidebarContent = () => (
    <>
      <div className="flex-1 flex flex-col min-h-0">
        <div className="flex items-center justify-center h-28 flex-shrink-0 px-6 border-b border-slate-100 relative">
          <Link to="/" className="flex items-center group">
            <img src="/logo.png" alt="HealthVerse AI Logo" className="h-20 w-auto" />
          </Link>
          
          <button 
            className="md:hidden absolute right-4 p-2 rounded-lg text-slate-400 hover:text-slate-600 hover:bg-slate-100 transition-colors"
            onClick={() => setSidebarOpen(false)}
          >
            <X className="w-5 h-5" />
          </button>
        </div>
        
        <div className="flex-1 flex flex-col overflow-y-auto custom-scrollbar">
          <nav className="flex-1 px-4 py-6 space-y-8">
            {navigationGroups.map((group) => (
              <div key={group.title}>
                <h3 className="px-3 text-xs font-bold text-slate-400 uppercase tracking-widest mb-3">
                  {group.title}
                </h3>
                <div className="space-y-1">
                  {group.items.map((item) => {
                    const isActive = location.pathname.startsWith(item.href);
                    return (
                      <Link
                        key={item.name}
                        to={item.href}
                        className={`group flex items-center px-3 py-2.5 text-sm font-medium rounded-xl transition-all duration-300 relative overflow-hidden ${
                          isActive
                            ? 'bg-primary-50 text-primary-700 font-bold'
                            : 'text-slate-600 hover:bg-slate-50 hover:text-slate-900'
                        }`}
                      >
                        {isActive && (
                          <motion.div 
                            layoutId="sidebar-active"
                            className="absolute left-0 top-0 bottom-0 w-1 bg-primary-600 rounded-r-full hidden md:block" 
                          />
                        )}
                        <item.icon
                          className={`mr-3 flex-shrink-0 h-5 w-5 transition-all duration-300 ${
                            isActive ? 'text-primary-600' : 'text-slate-400 group-hover:text-slate-500 group-hover:scale-110'
                          }`}
                          aria-hidden="true"
                        />
                        <span className="relative z-10">{item.name}</span>
                        
                        {!isActive && (
                          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-slate-100/50 to-transparent -translate-x-full group-hover:translate-x-full transition-transform duration-700 ease-in-out hidden md:block" />
                        )}
                      </Link>
                    );
                  })}
                </div>
              </div>
            ))}
          </nav>
        </div>
        
        <div className="flex-shrink-0 flex border-t border-slate-100 p-4 bg-slate-50/50">
          <button onClick={logout} className="flex-shrink-0 w-full group block text-left bg-white p-3 rounded-xl border border-slate-200 shadow-sm hover:shadow-md hover:border-red-200 transition-all">
            <div className="flex items-center">
              <div className="inline-block h-10 w-10 rounded-full bg-slate-100 flex items-center justify-center text-slate-500 group-hover:bg-red-50 group-hover:text-red-500 transition-colors">
                <User className="h-5 w-5" />
              </div>
              <div className="ml-3">
                <p className="text-sm font-bold text-slate-700 group-hover:text-slate-900 transition-colors">{user?.name || 'User'}</p>
                <p className="text-xs font-medium text-slate-500 group-hover:text-red-500 flex items-center gap-1 transition-colors mt-0.5">
                  <LogOut className="h-3 w-3" /> Sign out
                </p>
              </div>
            </div>
          </button>
        </div>
      </div>
    </>
  );

  return (
    <div className="min-h-screen bg-slate-50 flex">
      {/* Mobile Sidebar Overlay */}
      <AnimatePresence>
        {sidebarOpen && (
          <>
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              exit={{ opacity: 0 }}
              className="fixed inset-0 bg-slate-900/40 backdrop-blur-sm z-40 md:hidden"
              onClick={() => setSidebarOpen(false)}
            />
            <motion.div
              initial={{ x: '-100%' }}
              animate={{ x: 0 }}
              exit={{ x: '-100%' }}
              transition={{ type: 'spring', bounce: 0, duration: 0.4 }}
              className="fixed inset-y-0 left-0 w-72 bg-white shadow-2xl z-50 md:hidden flex flex-col"
            >
              <SidebarContent />
            </motion.div>
          </>
        )}
      </AnimatePresence>

      {/* Sidebar for desktop */}
      <div className="hidden md:flex md:w-72 md:flex-col md:fixed md:inset-y-0 bg-white border-r border-slate-200 shadow-sm z-20">
        <SidebarContent />
      </div>

      {/* Main Content Area */}
      <div className="md:pl-72 flex flex-col flex-1 min-h-screen relative">
        {/* Mobile header */}
        <div className="sticky top-0 z-30 md:hidden flex items-center justify-between px-4 py-3 bg-white/80 backdrop-blur-md border-b border-slate-200">
          <button
            type="button"
            className="p-2 -ml-2 rounded-lg text-slate-500 hover:text-slate-900 hover:bg-slate-100 transition-colors"
            onClick={() => setSidebarOpen(true)}
          >
            <Menu className="h-6 w-6" />
          </button>
          
          <img src="/logo.png" alt="HealthVerse AI Logo" className="h-12 w-auto" />
          
          <button onClick={() => setNotificationsOpen(!notificationsOpen)} className="p-2 rounded-lg text-slate-500 hover:text-primary-600 hover:bg-slate-100 transition-colors relative">
            <Bell className="h-6 w-6" />
            {unreadCount > 0 && (
              <span className="absolute top-1 right-1 h-2.5 w-2.5 rounded-full bg-red-500 ring-2 ring-white"></span>
            )}
          </button>
        </div>

        {/* Desktop Header Top Bar (Hidden on Mobile) */}
        <div className="hidden md:flex sticky top-0 z-30 h-16 bg-white/80 backdrop-blur-md border-b border-slate-200 items-center justify-end px-8">
          <div className="relative">
            <button 
              onClick={() => setNotificationsOpen(!notificationsOpen)} 
              className="p-2 rounded-full text-slate-500 hover:text-primary-600 hover:bg-primary-50 transition-colors relative"
            >
              <Bell className="h-6 w-6" />
              {unreadCount > 0 && (
                <span className="absolute top-1 right-1 h-3 w-3 rounded-full bg-red-500 ring-2 ring-white"></span>
              )}
            </button>
            
            {/* Notification Dropdown */}
            <AnimatePresence>
              {notificationsOpen && (
                <motion.div
                  initial={{ opacity: 0, y: 10, scale: 0.95 }}
                  animate={{ opacity: 1, y: 0, scale: 1 }}
                  exit={{ opacity: 0, y: 10, scale: 0.95 }}
                  transition={{ duration: 0.15 }}
                  className="absolute right-0 mt-2 w-80 bg-white rounded-xl shadow-2xl border border-slate-100 z-50 overflow-hidden"
                >
                  <div className="p-4 border-b border-slate-100 flex justify-between items-center bg-slate-50/50">
                    <h3 className="font-bold text-slate-800">Notifications</h3>
                    <span className="text-xs font-medium text-primary-600 bg-primary-50 px-2 py-1 rounded-full">
                      {unreadCount} new
                    </span>
                  </div>
                  <div className="max-h-[28rem] overflow-y-auto custom-scrollbar">
                    {notifications.length === 0 ? (
                      <div className="p-6 text-center text-slate-500 text-sm">
                        No notifications yet.
                      </div>
                    ) : (
                      <div className="divide-y divide-slate-50">
                        {notifications.map((notif) => (
                          <div 
                            key={notif.id} 
                            className={`p-4 transition-colors hover:bg-slate-50 ${!notif.read ? 'bg-primary-50/30' : ''}`}
                            onClick={() => {
                              if (!notif.read) markAsRead(notif.id);
                            }}
                          >
                            <div className="flex justify-between items-start mb-1">
                              <h4 className={`text-sm font-semibold ${!notif.read ? 'text-primary-900' : 'text-slate-700'}`}>
                                {notif.title}
                              </h4>
                              {!notif.read && (
                                <span className="h-2 w-2 rounded-full bg-primary-500 mt-1.5 flex-shrink-0"></span>
                              )}
                            </div>
                            <p className="text-xs text-slate-500 mb-2 leading-relaxed">{notif.message}</p>
                            <span className="text-[10px] font-medium text-slate-400 uppercase tracking-wider">
                              {new Date(notif.timestamp).toLocaleString()}
                            </span>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                </motion.div>
              )}
            </AnimatePresence>
          </div>
        </div>

        <main className="flex-1 overflow-hidden relative">
          <div className="py-6 md:py-8 h-full">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 md:px-8 h-full">
              {/* Page Transitions */}
              <AnimatePresence mode="wait">
                <motion.div
                  key={location.pathname}
                  initial={{ opacity: 0, y: 15, filter: 'blur(4px)' }}
                  animate={{ opacity: 1, y: 0, filter: 'blur(0px)' }}
                  exit={{ opacity: 0, y: -15, filter: 'blur(4px)' }}
                  transition={{ duration: 0.3, ease: 'easeInOut' }}
                  className="h-full"
                >
                  <Outlet />
                </motion.div>
              </AnimatePresence>
            </div>
          </div>
        </main>
        
        {/* Global Floating AI Button (Micro-interaction) */}
        {!location.pathname.includes('/chat') && (
          <Link to="/chat">
            <motion.div 
              initial={{ scale: 0, opacity: 0 }}
              animate={{ scale: 1, opacity: 1 }}
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              className="fixed bottom-6 right-6 md:bottom-8 md:right-8 z-30 group"
            >
              <div className="absolute inset-0 bg-primary-500 rounded-full blur-xl opacity-40 group-hover:opacity-70 animate-pulse transition-opacity" />
              <div className="relative bg-gradient-to-tr from-primary-600 to-primary-500 w-14 h-14 md:w-16 md:h-16 rounded-full flex items-center justify-center shadow-2xl text-white cursor-pointer border border-primary-400">
                <Sparkles className="h-6 w-6 md:h-7 md:w-7" />
                
                {/* Tooltip (hidden on mobile) */}
                <div className="hidden md:block absolute right-full mr-4 top-1/2 -translate-y-1/2 bg-slate-900 text-white text-xs font-bold px-3 py-1.5 rounded-lg opacity-0 pointer-events-none group-hover:opacity-100 transition-opacity whitespace-nowrap shadow-xl">
                  Ask AI Assistant
                  <div className="absolute top-1/2 -translate-y-1/2 left-full border-4 border-transparent border-l-slate-900" />
                </div>
              </div>
            </motion.div>
          </Link>
        )}
      </div>
    </div>
  );
};

export default DashboardLayout;
