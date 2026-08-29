import React from 'react';
import { Link } from 'react-router-dom';
import { 
  Activity, Shield, HeartPulse, BrainCircuit, FileText, Apple, Dumbbell, Pill, Moon, 
  ArrowRight, ChevronRight, Zap, Database, Network, TrendingUp, UserCheck, CheckCircle2
} from 'lucide-react';

const LOGO_URL = "/logo.png";

const Landing = () => {
  return (
    <div className="min-h-screen bg-slate-50 font-sans selection:bg-primary-100 selection:text-primary-900 overflow-hidden">
      
      {/* Navbar */}
      <nav className="fixed top-0 w-full z-50 bg-white/80 backdrop-blur-md border-b border-slate-200 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-24">
            <div className="flex items-center">
              <a href="#home" className="flex-shrink-0 flex items-center gap-2">
                <img src={LOGO_URL} alt="HealthVerse AI" className="h-20 w-auto" />
              </a>
              <div className="hidden md:ml-10 md:flex md:space-x-8">
                <a href="#home" className="text-slate-600 hover:text-primary-600 px-3 py-2 text-sm font-medium transition-colors">Home</a>
                <a href="#features" className="text-slate-600 hover:text-primary-600 px-3 py-2 text-sm font-medium transition-colors">Features</a>
                <a href="#how-it-works" className="text-slate-600 hover:text-primary-600 px-3 py-2 text-sm font-medium transition-colors">How It Works</a>
                <a href="#ai-agents" className="text-slate-600 hover:text-primary-600 px-3 py-2 text-sm font-medium transition-colors">AI Agents</a>
                <a href="#about" className="text-slate-600 hover:text-primary-600 px-3 py-2 text-sm font-medium transition-colors">About</a>
              </div>
            </div>
            <div className="flex items-center gap-4">
              <Link to="/login" className="text-sm font-medium text-slate-600 hover:text-primary-600 transition-colors">
                Log in
              </Link>
              <Link
                to="/register"
                className="inline-flex items-center justify-center px-5 py-2.5 border border-transparent rounded-full shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 transition-colors"
              >
                Sign up
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <section id="home" className="relative pt-32 pb-20 lg:pt-48 lg:pb-32">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative">
          <div className="max-w-3xl mx-auto text-center">
            <div className="inline-flex items-center px-3 py-1 rounded-full bg-accent-50 text-accent-700 font-medium text-sm mb-6 border border-accent-100">
              <SparklesIcon className="w-4 h-4 mr-2" /> HealthVerse AI Platform
            </div>
            <h1 className="text-4xl tracking-tight font-extrabold text-slate-900 sm:text-5xl md:text-6xl leading-snug md:leading-tight">
              <span className="block mb-2">Your Health,</span>
              <span className="block text-transparent bg-clip-text bg-gradient-to-r from-primary-600 to-secondary-600 pb-4 leading-normal">
                Powered by Intelligence.
              </span>
            </h1>
            <p className="mt-6 text-lg text-slate-500 max-w-2xl mx-auto">
              Predictive health monitoring, personalized wellness, and AI-driven insights to help you maintain a healthier lifestyle and optimize your daily routines.
            </p>
            <div className="mt-10 flex flex-col sm:flex-row gap-4 justify-center">
              <Link
                to="/register"
                className="px-8 py-4 border border-transparent text-lg font-medium rounded-full text-white bg-primary-600 hover:bg-primary-700 shadow-lg shadow-primary-500/30 transition-colors flex items-center justify-center"
              >
                Get Started <ArrowRight className="ml-2 w-5 h-5" />
              </Link>
              <a
                href="#features"
                className="px-8 py-4 border-2 border-slate-200 text-lg font-medium rounded-full text-slate-700 bg-white hover:border-slate-300 hover:bg-slate-50 transition-colors flex items-center justify-center"
              >
                Explore HealthVerse
              </a>
            </div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="py-24 bg-white relative">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <h2 className="text-base text-primary-600 font-semibold tracking-wide uppercase">Capabilities</h2>
            <p className="mt-2 text-3xl leading-8 font-extrabold tracking-tight text-slate-900 sm:text-4xl">
              A comprehensive health ecosystem
            </p>
            <p className="mt-4 max-w-2xl text-xl text-slate-500 mx-auto">
              Everything you need to monitor, analyze, and optimize your wellness in one intelligent platform.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
            <FeatureCard icon={Shield} title="Predictive Wellness" desc="Identify health trends and receive early warnings before issues escalate." />
            <FeatureCard icon={BrainCircuit} title="AI Health Assistant" desc="Interact with our intelligent chat for instant, personalized wellness guidance." />
            <FeatureCard icon={UserCheck} title="Digital Health Twin" desc="A real-time virtual representation of your complete health profile and habits." />
            <FeatureCard icon={FileText} title="Medical Reports" desc="Upload your PDFs for instant, easy-to-understand summaries and analysis." />
            <FeatureCard icon={Apple} title="Personalized Nutrition" desc="AI-generated dietary targets based on your unique profile and goals." />
            <FeatureCard icon={Dumbbell} title="Fitness Planning" desc="Adaptive activity routines that evolve with your physical progression." />
            <FeatureCard icon={Pill} title="Medicine Management" desc="Smart reminders and adherence tracking for your prescriptions." />
            <FeatureCard icon={Moon} title="Sleep & Recovery" desc="Monitor sleep patterns to optimize your body's essential recovery time." />
          </div>
        </div>
      </section>

      {/* How It Works Section */}
      <section id="how-it-works" className="py-24 bg-slate-50 relative">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-3xl mx-auto mb-20">
            <h2 className="text-base text-secondary-600 font-semibold tracking-wide uppercase">Workflow</h2>
            <p className="mt-2 text-3xl leading-8 font-extrabold tracking-tight text-slate-900 sm:text-4xl">
              How HealthVerse works
            </p>
          </div>

          <div className="relative max-w-4xl mx-auto">
            {/* Connecting Line */}
            <div className="absolute left-[27px] md:left-1/2 md:-ml-[1px] top-0 bottom-0 w-0.5 bg-gradient-to-b from-primary-200 via-secondary-200 to-accent-200"></div>

            <div className="space-y-16">
              <WorkflowStep number={1} title="Health Data" desc="Connect your devices or manually input your daily metrics, medical reports, and symptoms." icon={Database} align="right" />
              <WorkflowStep number={2} title="AI Analysis" desc="Our intelligent core processes your data securely to identify patterns and correlations." icon={BrainCircuit} align="left" />
              <WorkflowStep number={3} title="Multi-Agent Collaboration" desc="Specialized AI agents (Nutrition, Fitness, Sleep) work together to build a holistic view." icon={Network} align="right" />
              <WorkflowStep number={4} title="Predictive Insights" desc="Receive proactive warnings and score changes based on your health trajectory." icon={TrendingUp} align="left" />
              <WorkflowStep number={5} title="Personalized Wellness Plan" desc="Get an actionable, daily timeline of tailored recommendations to improve your health." icon={HeartPulse} align="right" />
            </div>
          </div>
        </div>
      </section>

      {/* AI Agents Section */}
      <section id="ai-agents" className="py-24 bg-slate-900 text-white relative overflow-hidden">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
          <div className="text-center max-w-3xl mx-auto mb-16">
            <h2 className="text-base text-accent-400 font-semibold tracking-wide uppercase">Under The Hood</h2>
            <p className="mt-2 text-3xl leading-8 font-extrabold tracking-tight sm:text-4xl">
              Multi-Agent Architecture
            </p>
            <p className="mt-4 max-w-2xl text-lg text-slate-400 mx-auto">
              HealthVerse isn't just one AI. It's a team of specialized intelligent agents working collaboratively to optimize your life.
            </p>
          </div>

          <div className="relative flex flex-col items-center max-w-5xl mx-auto pt-10 pb-20">
            {/* Top Node */}
            <AgentNode title="Planning Agent" role="Orchestrator" isTop />
            
            {/* Middle Row */}
            <div className="flex w-full justify-between mt-12 md:mt-16">
              <AgentNode title="Health Agent" role="Diagnostics" />
              <AgentNode title="Nutrition Agent" role="Dietary" />
              <AgentNode title="Fitness Agent" role="Physical" />
            </div>

            {/* Bottom Row */}
            <div className="flex w-full justify-between mt-8 md:mt-12">
              <AgentNode title="Report Agent" role="Analysis" />
              <AgentNode title="Medicine Agent" role="Adherence" />
              <AgentNode title="Wellness Agent" role="Recovery" />
            </div>
          </div>
        </div>
      </section>

      {/* About Section */}
      <section id="about" className="py-24 bg-white relative">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center max-w-4xl mx-auto">
            <h2 className="text-base text-primary-600 font-semibold tracking-wide uppercase">About</h2>
            <p className="mt-2 text-3xl leading-8 font-extrabold tracking-tight text-slate-900 sm:text-4xl mb-8">
              What is HealthVerse AI?
            </p>
            <div className="text-lg text-slate-600 mx-auto text-left md:text-center leading-relaxed space-y-4">
              <p>
                HealthVerse AI is an intelligent digital healthcare ecosystem that uses Agentic AI, predictive analytics, and personalized health monitoring to support preventive healthcare.
              </p>
              <p>
                It integrates health data, medical reports, symptoms, lifestyle patterns, nutrition, fitness, sleep, and medication information to generate personalized wellness insights.
              </p>
              <p>
                Multiple AI agents collaborate to analyze user data, identify potential health and wellness risks, explain medical reports, and create personalized wellness plans through a unified platform.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="bg-primary-600 py-16">
        <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
          <h2 className="text-3xl font-extrabold text-white sm:text-4xl mb-4">
            Ready to take control of your health?
          </h2>
          <p className="text-xl text-primary-100 mb-8">
            Join HealthVerse AI today and experience the future of personalized wellness.
          </p>
          <Link
            to="/register"
            className="inline-flex items-center justify-center px-8 py-4 border border-transparent text-lg font-medium rounded-full text-primary-700 bg-white hover:bg-slate-50 shadow-xl transition-colors"
          >
            Create Your Account
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-white text-slate-600 pt-16 pb-8 border-t border-slate-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-5 gap-12 md:gap-8 mb-12 mt-4">
            
            {/* Left Column (Brand) */}
            <div className="md:col-span-2 space-y-6">
              <Link to="/" className="inline-block">
                <img src={LOGO_URL} alt="HealthVerse AI" className="h-16 w-auto" />
              </Link>
              <div className="flex gap-4 items-center">
                <a href="#" className="text-slate-400 hover:text-primary-600 transition-colors">
                  <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path fillRule="evenodd" d="M12.315 2c2.43 0 2.784.013 3.808.06 1.064.049 1.791.218 2.427.465a4.902 4.902 0 011.772 1.153 4.902 4.902 0 011.153 1.772c.247.636.416 1.363.465 2.427.048 1.067.06 1.407.06 4.123v.08c0 2.643-.012 2.987-.06 4.043-.049 1.064-.218 1.791-.465 2.427a4.902 4.902 0 01-1.153 1.772 4.902 4.902 0 01-1.772 1.153c-.636.247-1.363.416-2.427.465-1.067.048-1.407.06-4.123.06h-.08c-2.643 0-2.987-.012-4.043-.06-1.064-.049-1.791-.218-2.427-.465a4.902 4.902 0 01-1.772-1.153 4.902 4.902 0 01-1.153-1.772c-.247-.636-.416-1.363-.465-2.427-.047-1.024-.06-1.379-.06-3.808v-.63c0-2.43.013-2.784.06-3.808.049-1.064.218-1.791.465-2.427a4.902 4.902 0 011.153-1.772A4.902 4.902 0 015.45 2.525c.636-.247 1.363-.416 2.427-.465C8.901 2.013 9.256 2 11.685 2h.63zm-.081 1.802h-.468c-2.456 0-2.784.011-3.807.058-.975.045-1.504.207-1.857.344-.467.182-.8.398-1.15.748-.35.35-.566.683-.748 1.15-.137.353-.3.882-.344 1.857-.047 1.023-.058 1.351-.058 3.807v.468c0 2.456.011 2.784.058 3.807.045.975.207 1.504.344 1.857.182.466.399.8.748 1.15.35.35.683.566 1.15.748.353.137.882.3 1.857.344 1.054.048 1.37.058 4.041.058h.08c2.597 0 2.917-.01 3.96-.058.976-.045 1.505-.207 1.858-.344.466-.182.8-.398 1.15-.748.35-.35.566-.683.748-1.15.137-.353.3-.882.344-1.857.048-1.055.058-1.37.058-4.041v-.08c0-2.597-.01-2.917-.058-3.96-.045-.976-.207-1.505-.344-1.858a3.097 3.097 0 00-.748-1.15 3.098 3.098 0 00-1.15-.748c-.353-.137-.882-.3-1.857-.344-1.023-.047-1.351-.058-3.807-.058zM12 6.865a5.135 5.135 0 110 10.27 5.135 5.135 0 010-10.27zm0 1.802a3.333 3.333 0 100 6.666 3.333 3.333 0 000-6.666zm5.338-3.205a1.2 1.2 0 110 2.4 1.2 1.2 0 010-2.4z" clipRule="evenodd" /></svg>
                </a>
                <a href="#" className="text-slate-400 hover:text-primary-600 transition-colors">
                  <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 24 24"><path fillRule="evenodd" d="M19 0h-14c-2.761 0-5 2.239-5 5v14c0 2.761 2.239 5 5 5h14c2.762 0 5-2.239 5-5v-14c0-2.761-2.238-5-5-5zm-11 19h-3v-11h3v11zm-1.5-12.268c-.966 0-1.75-.79-1.75-1.764s.784-1.764 1.75-1.764 1.75.79 1.75 1.764-.783 1.764-1.75 1.764zm13.5 12.268h-3v-5.604c0-3.368-4-3.113-4 0v5.604h-3v-11h3v1.765c1.396-2.586 7-2.777 7 2.476v6.759z" clipRule="evenodd" /></svg>
                </a>
              </div>
              <p className="text-slate-500 text-sm max-w-sm">
                Predictive health monitoring and personalized wellness insights driven by Agentic AI.
              </p>
            </div>

            {/* PLATFORM Column */}
            <div>
              <h4 className="text-xs font-bold text-slate-900 uppercase tracking-widest mb-6">Platform</h4>
              <ul className="space-y-4 text-sm">
                <li><a href="#features" className="text-slate-500 hover:text-primary-600 transition-colors">Features</a></li>
                <li><a href="#ai-agents" className="text-slate-500 hover:text-primary-600 transition-colors">AI Agents</a></li>
                <li><a href="#how-it-works" className="text-slate-500 hover:text-primary-600 transition-colors">How It Works</a></li>
              </ul>
            </div>

            {/* COMPANY Column */}
            <div>
              <h4 className="text-xs font-bold text-slate-900 uppercase tracking-widest mb-6">Company</h4>
              <ul className="space-y-4 text-sm">
                <li><a href="#about" className="text-slate-500 hover:text-primary-600 transition-colors">About Us</a></li>
                <li><a href="#" className="text-slate-500 hover:text-primary-600 transition-colors">Contact Us</a></li>
              </ul>
            </div>

            {/* LEGAL Column */}
            <div>
              <h4 className="text-xs font-bold text-slate-900 uppercase tracking-widest mb-6">Legal</h4>
              <ul className="space-y-4 text-sm">
                <li><a href="#" className="text-slate-500 hover:text-primary-600 transition-colors">Privacy Policy</a></li>
                <li><a href="#" className="text-slate-500 hover:text-primary-600 transition-colors">Terms of Service</a></li>
              </ul>
            </div>
            
          </div>

          <div className="pt-8 border-t border-slate-200 flex flex-col md:flex-row justify-between items-start md:items-center gap-4 text-xs text-slate-500">
            <p>© {new Date().getFullYear()} HealthVerse AI. A product of your team.</p>
            <p className="max-w-2xl text-left md:text-right">
              HealthVerse AI is a predictive wellness platform. It does not provide professional medical diagnosis, advice, or treatment. Always seek the advice of your physician or other qualified health provider with any questions you may have regarding a medical condition.
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
};

/* --- Helper Components --- */

const SparklesIcon = ({ className }: { className?: string }) => (
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className={className}>
    <path fillRule="evenodd" d="M9 4.5a.75.75 0 01.721.544l.813 2.846a3.75 3.75 0 002.576 2.576l2.846.813a.75.75 0 010 1.442l-2.846.813a3.75 3.75 0 00-2.576 2.576l-.813 2.846a.75.75 0 01-1.442 0l-.813-2.846a3.75 3.75 0 00-2.576-2.576l-2.846-.813a.75.75 0 010-1.442l2.846-.813A3.75 3.75 0 007.466 7.89l.813-2.846A.75.75 0 019 4.5zM18 1.5a.75.75 0 01.728.568l.258 1.036c.236.94.97 1.674 1.91 1.91l1.036.258a.75.75 0 010 1.456l-1.036.258c-.94.236-1.674.97-1.91 1.91l-.258 1.036a.75.75 0 01-1.456 0l-.258-1.036a2.625 2.625 0 00-1.91-1.91l-1.036-.258a.75.75 0 010-1.456l1.036-.258a2.625 2.625 0 001.91-1.91l.258-1.036A.75.75 0 0118 1.5zM16.5 15a.75.75 0 01.712.513l.394 1.183c.15.447.5.799.948.948l1.183.395a.75.75 0 010 1.422l-1.183.395c-.447.15-.799.5-.948.948l-.395 1.183a.75.75 0 01-1.422 0l-.395-1.183a1.5 1.5 0 00-.948-.948l-1.183-.395a.75.75 0 010-1.422l1.183-.395c.447-.15.799-.5.948-.948l.395-1.183A.75.75 0 0116.5 15z" clipRule="evenodd" />
  </svg>
);

const FeatureCard = ({ icon: Icon, title, desc }: any) => (
  <div className="bg-white p-8 rounded-2xl shadow-sm border border-slate-100 flex flex-col items-start hover:shadow-md hover:-translate-y-1 transition-all duration-300 group">
    <div className="h-12 w-12 rounded-xl bg-slate-50 flex items-center justify-center mb-6 group-hover:bg-primary-50 transition-colors">
      <Icon className="h-6 w-6 text-slate-700 group-hover:text-primary-600 transition-colors" />
    </div>
    <h3 className="text-xl font-bold text-slate-900 mb-3">{title}</h3>
    <p className="text-slate-500 text-sm leading-relaxed">{desc}</p>
  </div>
);

const WorkflowStep = ({ number, title, desc, icon: Icon, align }: any) => {
  const isRight = align === 'right';
  return (
    <div className={`relative flex items-center justify-between md:justify-normal ${isRight ? 'flex-row-reverse md:flex-row' : 'flex-row md:flex-row-reverse'}`}>
      <div className={`hidden md:block w-5/12 ${isRight ? 'text-right pr-8' : 'text-left pl-8'}`}>
        <h3 className="text-2xl font-bold text-slate-900 mb-2">{title}</h3>
        <p className="text-slate-500">{desc}</p>
      </div>
      
      <div className="relative z-10 flex items-center justify-center w-14 h-14 rounded-full bg-white border-4 border-primary-100 shadow-md flex-shrink-0 mx-auto md:mx-0">
        <span className="text-lg font-bold text-primary-600">{number}</span>
      </div>
      
      <div className={`w-[80%] pl-6 md:w-5/12 ${isRight ? 'md:pl-8 text-left' : 'md:pr-8 md:text-right'}`}>
        <div className="md:hidden">
           <h3 className="text-xl font-bold text-slate-900 mb-2">{title}</h3>
           <p className="text-slate-500 text-sm">{desc}</p>
        </div>
        <div className={`hidden md:flex ${isRight ? 'justify-start' : 'justify-end'}`}>
          <div className="w-16 h-16 rounded-2xl bg-white shadow-sm border border-slate-100 flex items-center justify-center">
            <Icon className="w-8 h-8 text-slate-400" />
          </div>
        </div>
      </div>
    </div>
  );
};

const AgentNode = ({ title, role, isTop = false }: any) => (
  <div className={`relative z-20 bg-slate-800 rounded-2xl p-4 border border-slate-700 shadow-xl flex flex-col items-center w-[30%] md:w-40 text-center ${isTop ? 'ring-2 ring-accent-500' : ''}`}>
    <div className={`w-10 h-10 rounded-full mb-3 flex items-center justify-center ${isTop ? 'bg-accent-500/20' : 'bg-slate-700'}`}>
      <BrainCircuit className={`w-5 h-5 ${isTop ? 'text-accent-400' : 'text-slate-300'}`} />
    </div>
    <h4 className="text-sm font-bold text-white leading-tight">{title}</h4>
    <span className="text-xs text-slate-400 mt-1">{role}</span>
  </div>
);

export default Landing;
