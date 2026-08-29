import React, { useEffect, useState } from 'react';
import { twinApi } from '../services/twinApi';
import { TwinMetrics } from '../types/twin';
import { motion } from 'framer-motion';
import { 
  Accessibility, Loader2, Activity, Scale, Pill, Apple, ShieldCheck, AlertCircle,
  BrainCircuit, Heart, Zap, Sparkles, History
} from 'lucide-react';
import { 
  LineChart, Line, AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  Radar, RadarChart, PolarGrid, PolarAngleAxis, PolarRadiusAxis
} from 'recharts';

const container = {
  hidden: { opacity: 0 },
  show: { opacity: 1, transition: { staggerChildren: 0.1 } }
};

const item = {
  hidden: { opacity: 0, y: 20 },
  show: { opacity: 1, y: 0, transition: { duration: 0.5, ease: 'easeOut' } }
};

const DigitalHealthTwin = () => {
  const [metrics, setMetrics] = useState<TwinMetrics | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchTwinData = async () => {
      try {
        const data = await twinApi.getTwinMetrics();
        setMetrics(data);
      } catch (err: any) {
        console.error('Failed to fetch twin metrics:', err);
        setError(err.response?.data?.message || 'Failed to connect to the Digital Twin service.');
      } finally {
        setIsLoading(false);
      }
    };
    fetchTwinData();
  }, []);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Loader2 className="h-8 w-8 text-primary-600 animate-spin" />
      </div>
    );
  }

  if (error || !metrics) {
    return (
      <div className="text-center py-12 flex flex-col items-center">
        <div className="inline-flex items-center justify-center w-16 h-16 rounded-full bg-red-50 mb-4">
          <AlertCircle className="h-8 w-8 text-red-500" />
        </div>
        <h3 className="text-lg font-bold text-slate-900 mb-2">Service Unavailable</h3>
        <p className="text-slate-500 max-w-md mx-auto">{error || 'Failed to load Digital Health Twin data. Please try again later.'}</p>
      </div>
    );
  }

  return (
    <motion.div 
      initial="hidden"
      animate="show"
      variants={container}
      className="max-w-7xl mx-auto space-y-6 pb-12"
    >
      {/* Header */}
      <motion.div variants={item} className="flex flex-col md:flex-row md:items-end justify-between gap-4 bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 tracking-tight flex items-center gap-3">
            <div className="p-2 bg-primary-50 rounded-xl">
              <Accessibility className="h-8 w-8 text-primary-600" />
            </div>
            Digital Health Twin
          </h1>
          <p className="text-slate-500 mt-2">
            A real-time multidimensional virtual representation of your wellness profile.
          </p>
        </div>
        
        <div className="bg-slate-50 px-6 py-4 rounded-xl border border-slate-200 flex flex-col items-end gap-1">
          <span className="text-xs font-bold text-slate-400 uppercase tracking-widest">Overall Twin Score</span>
          <div className="flex items-baseline gap-2">
            <span className="text-3xl font-black text-primary-600">{metrics.overallWellnessScore}</span>
            <span className="text-sm font-medium text-slate-500">/ 100</span>
          </div>
        </div>
      </motion.div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
        
        {/* Central Twin Visualization & Status */}
        <div className="lg:col-span-5 space-y-6">
          <motion.div variants={item} className="bg-white rounded-2xl shadow-sm border border-slate-100 p-8 flex flex-col items-center relative overflow-hidden">
            <h3 className="text-sm font-bold text-slate-500 uppercase tracking-widest mb-4 w-full text-left z-10">Twin State Model</h3>
            
            <div className="relative w-full h-[300px] flex items-center justify-center z-10">
              {metrics.status === 'insufficient data' ? (
                <div className="text-center text-slate-400">
                  <Accessibility className="w-12 h-12 mx-auto mb-2 opacity-50" />
                  <p>Insufficient data to model your twin.</p>
                  <p className="text-xs mt-1">Please log your health data to activate.</p>
                </div>
              ) : (
                <ResponsiveContainer width="100%" height="100%">
                  <RadarChart cx="50%" cy="50%" outerRadius="70%" data={metrics.radarData}>
                    <PolarGrid stroke="#e2e8f0" />
                    <PolarAngleAxis dataKey="subject" tick={{ fill: '#64748b', fontSize: 12, fontWeight: 500 }} />
                    <Radar name="Twin" dataKey="A" stroke="#3b82f6" strokeWidth={2} fill="#3b82f6" fillOpacity={0.2} />
                  </RadarChart>
                </ResponsiveContainer>
              )}
              {/* Central glowing icon */}
              {metrics.status !== 'insufficient data' && (
              <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
                <div className="relative">
                  <div className="absolute inset-0 bg-primary-500 rounded-full blur-xl opacity-20 animate-pulse"></div>
                  <Accessibility className="w-12 h-12 text-primary-500 relative z-10" />
                </div>
              </div>
              )}
            </div>

            <div className="w-full mt-6 p-4 bg-slate-50 rounded-xl border border-slate-100 flex items-start gap-3">
               <ShieldCheck className="w-6 h-6 text-primary-500 mt-0.5 shrink-0" />
               <div>
                  <h4 className="font-semibold text-slate-900 text-sm">Status: {metrics.status === 'insufficient data' ? 'Pending Data' : 'Optimal Trajectory'}</h4>
                  <p className="text-xs text-slate-600 mt-1 leading-relaxed">
                    {metrics.status === 'insufficient data' ? 'Log your daily activity, weight, and sleep to unlock predictive models.' : 'Your digital twin shows stable mental and physical states. Recovery requires attention based on recent sleep data.'}
                  </p>
               </div>
            </div>
          </motion.div>

          <motion.div variants={item} className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
            <h3 className="text-sm font-bold text-slate-500 uppercase tracking-widest mb-6">System Adherence</h3>
            <div className="space-y-6">
              <div>
                <div className="flex justify-between items-end mb-2">
                  <span className="text-sm font-semibold text-slate-700 flex items-center gap-2"><Apple className="w-4 h-4 text-warning-500" /> Nutrition Plan</span>
                  <span className="text-sm font-bold text-slate-900">{metrics.nutritionAdherence}%</span>
                </div>
                <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden">
                  <motion.div initial={{ width: 0 }} animate={{ width: `${metrics.nutritionAdherence}%` }} transition={{ duration: 1, delay: 0.2 }} className="h-full bg-warning-500 rounded-full" />
                </div>
              </div>
              <div>
                <div className="flex justify-between items-end mb-2">
                  <span className="text-sm font-semibold text-slate-700 flex items-center gap-2"><Pill className="w-4 h-4 text-accent-500" /> Medications</span>
                  <span className="text-sm font-bold text-slate-900">{metrics.medicationAdherence}%</span>
                </div>
                <div className="h-2 w-full bg-slate-100 rounded-full overflow-hidden">
                  <motion.div initial={{ width: 0 }} animate={{ width: `${metrics.medicationAdherence}%` }} transition={{ duration: 1, delay: 0.4 }} className="h-full bg-accent-500 rounded-full" />
                </div>
              </div>
            </div>
          </motion.div>
        </div>

        {/* Right Column: Timelines & Charts */}
        <div className="lg:col-span-7 space-y-6">
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <motion.div variants={item} className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6 h-[320px] flex flex-col">
              <div className="flex items-center justify-between mb-4">
                <h3 className="font-bold text-slate-900 text-sm uppercase tracking-widest">Biometrics</h3>
                <Scale className="h-4 w-4 text-slate-400" />
              </div>
              <div className="flex-1 min-h-0">
                <ResponsiveContainer width="100%" height="100%">
                  <AreaChart data={metrics.weightTrend} margin={{ top: 10, right: 0, left: -20, bottom: 0 }}>
                    <defs>
                      <linearGradient id="colorWeight" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.3}/>
                        <stop offset="95%" stopColor="#3b82f6" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                    <XAxis dataKey="date" axisLine={false} tickLine={false} tick={{ fill: '#94a3b8', fontSize: 11 }} dy={10} />
                    <YAxis yAxisId="left" domain={['dataMin - 2', 'dataMax + 2']} axisLine={false} tickLine={false} tick={{ fill: '#94a3b8', fontSize: 11 }} />
                    <Tooltip contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }} />
                    <Area yAxisId="left" type="monotone" dataKey="weight" name="Weight (kg)" stroke="#3b82f6" strokeWidth={3} fillOpacity={1} fill="url(#colorWeight)" />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </motion.div>

            <motion.div variants={item} className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6 h-[320px] flex flex-col">
              <div className="flex items-center justify-between mb-4">
                <h3 className="font-bold text-slate-900 text-sm uppercase tracking-widest">Energy vs Recovery</h3>
                <Activity className="h-4 w-4 text-slate-400" />
              </div>
              <div className="flex-1 min-h-0">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart data={metrics.activitySleepTrend} margin={{ top: 10, right: 0, left: -20, bottom: 0 }}>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                    <XAxis dataKey="date" axisLine={false} tickLine={false} tick={{ fill: '#94a3b8', fontSize: 11 }} dy={10} />
                    <YAxis yAxisId="left" axisLine={false} tickLine={false} tick={{ fill: '#94a3b8', fontSize: 11 }} />
                    <Tooltip contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)' }} />
                    <Line yAxisId="left" type="monotone" dataKey="activity" name="Activity" stroke="#10b981" strokeWidth={3} dot={false} activeDot={{ r: 6 }} />
                    <Line yAxisId="left" type="monotone" dataKey="sleep" name="Sleep" stroke="#8b5cf6" strokeWidth={3} dot={false} activeDot={{ r: 6 }} />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            </motion.div>
          </div>

          <motion.div variants={item} className="bg-white rounded-2xl shadow-sm border border-slate-100 p-6">
            <div className="flex items-center gap-2 mb-6">
              <History className="w-5 h-5 text-slate-400" />
              <h3 className="text-sm font-bold text-slate-500 uppercase tracking-widest">Health Journey Timeline</h3>
            </div>
            
            <div className="space-y-6 relative before:absolute before:inset-0 before:ml-5 before:-translate-x-px md:before:mx-auto md:before:translate-x-0 before:h-full before:w-0.5 before:bg-gradient-to-b before:from-transparent before:via-slate-200 before:to-transparent">
              {metrics.journeyEvents.map((event, idx) => {
                let Icon = Sparkles;
                let colorClass = 'text-primary-500';
                let bgClass = 'bg-primary-50';
                
                if (event.type === 'warning') {
                  Icon = AlertCircle;
                  colorClass = 'text-warning-500';
                  bgClass = 'bg-warning-50';
                } else if (event.type === 'success') {
                  Icon = ShieldCheck;
                  colorClass = 'text-green-500';
                  bgClass = 'bg-green-50';
                }

                return (
                  <motion.div 
                    key={idx}
                    initial={{ opacity: 0, y: 10 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.4, delay: 0.6 + (idx * 0.1) }}
                    className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group is-active"
                  >
                    <div className={`flex items-center justify-center w-10 h-10 rounded-full border-4 border-white ${bgClass} shadow-sm shrink-0 md:order-1 md:group-odd:-translate-x-1/2 md:group-even:translate-x-1/2 z-10`}>
                      <Icon className={`w-4 h-4 ${colorClass}`} />
                    </div>
                    
                    <div className="w-[calc(100%-3rem)] md:w-[calc(50%-2rem)] p-4 rounded-xl border border-slate-100 bg-white shadow-sm hover:shadow-md transition-shadow">
                      <div className="flex items-center justify-between">
                        <h4 className="font-semibold text-sm text-slate-800">{event.event}</h4>
                        <span className="text-xs font-medium text-slate-400">{event.date}</span>
                      </div>
                    </div>
                  </motion.div>
                );
              })}
            </div>
          </motion.div>
          
        </div>
      </div>

      <div className="text-center mt-8">
        <p className="text-xs text-slate-400 inline-flex items-center gap-1">
          <AlertCircle className="h-3 w-3" />
          The Digital Health Twin provides predictive wellness analysis and is not a clinical diagnostic tool.
        </p>
      </div>
    </motion.div>
  );
};

export default DigitalHealthTwin;
