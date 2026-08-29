import React, { useState, useRef, useEffect } from 'react';
import { aiApi } from '../services/aiApi';
import { ChatMessage } from '../types/ai';
import { motion, AnimatePresence } from 'framer-motion';
import { Send, Loader2, Bot, User, Trash2, AlertTriangle, Sparkles, MessageSquare } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const AIChat = () => {
  const { user } = useAuth();
  const [messages, setMessages] = useState<ChatMessage[]>([{
    id: 'initial',
    role: 'ai',
    content: `Hello ${user?.name.split(' ')[0] || ''}! I'm your HealthVerse AI wellness assistant. How can I help you improve your healthy lifestyle today?`,
    timestamp: new Date().toISOString()
  }]);
  
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const suggestedQuestions = [
    "How can I improve my sleep?",
    "What are some healthy meal options?",
    "How can I improve my daily activity?",
    "What are good exercises for back pain?"
  ];

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages, isLoading]);

  const handleSend = async (messageText: string) => {
    if (!messageText.trim()) return;

    const userMessage: ChatMessage = {
      id: Date.now().toString(),
      role: 'user',
      content: messageText,
      timestamp: new Date().toISOString()
    };

    const newMessages = [...messages, userMessage];
    setMessages(newMessages);
    setInput('');
    setIsLoading(true);

    try {
      const response = await aiApi.chat({
        message: messageText,
        history: newMessages
      });

      const aiMessage: ChatMessage = {
        id: (Date.now() + 1).toString(),
        role: 'ai',
        content: response.message,
        timestamp: new Date().toISOString()
      };

      if (response.disclaimer && response.disclaimer.includes("Agents Involved")) {
        aiMessage.content = aiMessage.content + "\n\n_(" + response.disclaimer + ")_";
      }

      setMessages([...newMessages, aiMessage]);
    } catch (error) {
      const errorMessage: ChatMessage = {
        id: (Date.now() + 1).toString(),
        role: 'ai',
        content: "I'm sorry, I'm having trouble connecting right now. Please try again later.",
        timestamp: new Date().toISOString()
      };
      setMessages([...newMessages, errorMessage]);
    } finally {
      setIsLoading(false);
    }
  };

  const clearChat = () => {
    setMessages([{
      id: Date.now().toString(),
      role: 'ai',
      content: "Chat history cleared. How can I help you?",
      timestamp: new Date().toISOString()
    }]);
  };

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-4xl mx-auto flex flex-col h-[calc(100vh-8rem)] pb-6"
    >
      <div className="flex items-center justify-between bg-white p-6 rounded-t-2xl shadow-sm border border-slate-100 mb-0 border-b-0 z-10">
        <div>
          <h1 className="text-2xl font-extrabold text-slate-900 flex items-center gap-3">
            <div className="p-2 bg-primary-50 rounded-xl">
              <MessageSquare className="h-6 w-6 text-primary-600" />
            </div>
            AI Health Assistant
          </h1>
          <p className="text-slate-500 mt-1 text-sm">Ask questions about nutrition, fitness, and general wellness.</p>
        </div>
        <button 
          onClick={clearChat}
          className="flex items-center gap-2 px-4 py-2 text-sm font-bold text-slate-500 hover:text-red-600 hover:bg-red-50 rounded-xl transition-colors border border-transparent hover:border-red-100"
        >
          <Trash2 className="h-4 w-4" /> Clear Chat
        </button>
      </div>

      <div className="bg-amber-50 border-x border-b border-amber-200 text-amber-800 px-6 py-3 flex items-center gap-3 flex-shrink-0 z-10">
        <AlertTriangle className="h-5 w-5 flex-shrink-0" />
        <p className="text-xs font-medium">
          <strong>Notice:</strong> This AI provides wellness guidance and does not replace professional medical diagnosis.
        </p>
      </div>

      <div className="bg-white rounded-b-2xl shadow-sm border border-slate-100 border-t-0 flex flex-col flex-1 overflow-hidden relative">
        {/* Background watermark */}
        <div className="absolute inset-0 flex items-center justify-center opacity-5 pointer-events-none">
          <Sparkles className="w-64 h-64 text-primary-500" />
        </div>

        {/* Chat Messages */}
        <div className="flex-1 overflow-y-auto p-6 space-y-8 relative z-10">
          <AnimatePresence>
            {messages.map((msg, index) => (
              <motion.div 
                initial={{ opacity: 0, y: 10, scale: 0.95 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                transition={{ duration: 0.3 }}
                key={msg.id} 
                className={`flex ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}
              >
                <div className={`flex max-w-[85%] ${msg.role === 'user' ? 'flex-row-reverse' : 'flex-row'} gap-4`}>
                  <div className={`h-10 w-10 rounded-full flex items-center justify-center flex-shrink-0 shadow-sm ${
                    msg.role === 'user' ? 'bg-gradient-to-tr from-primary-600 to-primary-500' : 'bg-white border border-slate-200'
                  }`}>
                    {msg.role === 'user' ? <User className="h-5 w-5 text-white" /> : <Bot className="h-6 w-6 text-primary-600" />}
                  </div>
                  <div className={`px-5 py-4 shadow-sm ${
                    msg.role === 'user' 
                      ? 'bg-primary-600 text-white rounded-2xl rounded-tr-sm' 
                      : 'bg-white border border-slate-100 text-slate-800 rounded-2xl rounded-tl-sm'
                  }`}>
                    <p className="text-sm leading-relaxed whitespace-pre-wrap font-medium">{msg.content}</p>
                    <p className={`text-[10px] mt-2 text-right ${msg.role === 'user' ? 'text-primary-200' : 'text-slate-400'}`}>
                      {new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                    </p>
                  </div>
                </div>
              </motion.div>
            ))}
          </AnimatePresence>
          
          {isLoading && (
            <motion.div 
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              className="flex justify-start"
            >
              <div className="flex max-w-[80%] gap-4">
                <div className="h-10 w-10 rounded-full bg-white border border-slate-200 shadow-sm flex items-center justify-center flex-shrink-0">
                  <Bot className="h-6 w-6 text-primary-600" />
                </div>
                <div className="px-5 py-4 shadow-sm bg-white border border-slate-100 rounded-2xl rounded-tl-sm flex items-center gap-2">
                  <div className="flex gap-1.5">
                    <span className="w-2 h-2 bg-primary-400 rounded-full animate-bounce"></span>
                    <span className="w-2 h-2 bg-primary-400 rounded-full animate-bounce" style={{ animationDelay: '0.2s' }}></span>
                    <span className="w-2 h-2 bg-primary-400 rounded-full animate-bounce" style={{ animationDelay: '0.4s' }}></span>
                  </div>
                </div>
              </div>
            </motion.div>
          )}
          <div ref={messagesEndRef} />
        </div>

        {/* Suggested Questions & Input */}
        <div className="p-6 bg-slate-50 border-t border-slate-200 relative z-10">
          <AnimatePresence>
            {messages.length <= 2 && (
              <motion.div 
                initial={{ opacity: 0, height: 0 }}
                animate={{ opacity: 1, height: 'auto' }}
                exit={{ opacity: 0, height: 0 }}
                className="mb-4 flex flex-wrap gap-2"
              >
                {suggestedQuestions.map((q, idx) => (
                  <button
                    key={idx}
                    onClick={() => handleSend(q)}
                    disabled={isLoading}
                    className="text-xs font-bold bg-white border border-slate-200 text-slate-600 px-4 py-2 rounded-full hover:border-primary-300 hover:text-primary-700 hover:bg-primary-50 hover:shadow-sm transition-all"
                  >
                    {q}
                  </button>
                ))}
              </motion.div>
            )}
          </AnimatePresence>
          
          <form 
            onSubmit={(e) => { e.preventDefault(); handleSend(input); }} 
            className="flex items-center gap-3 relative"
          >
            <input
              type="text"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              placeholder="Ask anything about health and wellness..."
              disabled={isLoading}
              className="flex-1 pl-6 pr-14 py-4 bg-white border border-slate-200 rounded-full focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent transition-all text-sm font-medium shadow-sm text-slate-800 placeholder-slate-400"
            />
            <button
              type="submit"
              disabled={!input.trim() || isLoading}
              className="absolute right-2 h-10 w-10 rounded-full bg-primary-600 text-white flex items-center justify-center hover:bg-primary-700 hover:scale-105 disabled:opacity-50 disabled:hover:scale-100 disabled:cursor-not-allowed transition-all shadow-md"
            >
              <Send className="h-5 w-5 ml-1" />
            </button>
          </form>
        </div>
      </div>
    </motion.div>
  );
};

export default AIChat;
