import React, { useState } from 'react';
import { aiApi } from '../services/aiApi';
import { SymptomAnalysisResponse } from '../types/ai';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  ActivitySquare, Loader2, AlertCircle, CheckCircle2, 
  AlertTriangle, Lightbulb, Activity, ChevronRight
} from 'lucide-react';

const SymptomAnalyzer = () => {
  const [symptoms, setSymptoms] = useState('');
  const [duration, setDuration] = useState('');
  const [severity, setSeverity] = useState<'Mild' | 'Moderate' | 'Severe' | ''>('');
  
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [error, setError] = useState('');
  const [result, setResult] = useState<SymptomAnalysisResponse | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!symptoms.trim()) return;

    setError('');
    setIsAnalyzing(true);
    setResult(null);

    try {
      const response = await aiApi.analyzeSymptoms({
        symptoms,
        duration,
        severity: (severity as 'Mild' | 'Moderate' | 'Severe' | undefined),
      });
      setResult(response);
    } catch (err) {
      setError('An error occurred while analyzing your symptoms. Please try again later.');
    } finally {
      setIsAnalyzing(false);
    }
  };

  const handleReset = () => {
    setSymptoms('');
    setDuration('');
    setSeverity('');
    setResult(null);
    setError('');
  };

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-6xl mx-auto space-y-8 pb-12"
    >
      <div className="flex items-center justify-between bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 flex items-center gap-3">
            <div className="p-2 bg-primary-50 rounded-xl">
              <ActivitySquare className="h-8 w-8 text-primary-600" />
            </div>
            Symptom Analyzer
          </h1>
          <p className="text-slate-500 mt-2">Describe how you're feeling to receive AI-powered wellness insights.</p>
        </div>
      </div>

      <div className="bg-amber-50 border border-amber-200 text-amber-800 px-6 py-4 rounded-xl flex items-start gap-4 shadow-sm">
        <div className="p-2 bg-amber-100 rounded-full mt-0.5 shrink-0">
          <AlertTriangle className="h-5 w-5" />
        </div>
        <p className="text-sm leading-relaxed">
          <strong>Medical Disclaimer:</strong> This intelligent tool provides general wellness guidance based on common symptom patterns. It is <strong>not</strong> a medical diagnosis and does not replace professional medical advice. If you are experiencing a medical emergency, please call your local emergency services immediately.
        </p>
      </div>

      <AnimatePresence>
        {error && (
          <motion.div 
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-xl flex items-center gap-2"
          >
            <AlertCircle className="h-5 w-5 flex-shrink-0" />
            <span className="text-sm font-medium">{error}</span>
          </motion.div>
        )}
      </AnimatePresence>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
        
        {/* Input Form */}
        <div className="lg:col-span-5">
          <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden sticky top-24">
            <div className="p-6 border-b border-slate-100 bg-slate-50">
              <h3 className="text-sm font-bold text-slate-500 uppercase tracking-widest">Describe Symptoms</h3>
            </div>
            
            <form onSubmit={handleSubmit} className="p-6 space-y-6">
              <div>
                <label className="block text-sm font-bold text-slate-700 mb-2">What are you experiencing?</label>
                <textarea
                  value={symptoms}
                  onChange={(e) => setSymptoms(e.target.value)}
                  placeholder="E.g., I have a headache, a mild fever, and feel very tired..."
                  required
                  rows={5}
                  className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-primary-500 focus:bg-white transition-all resize-none text-slate-700 placeholder-slate-400"
                />
              </div>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2">Duration (Optional)</label>
                  <input
                    type="text"
                    value={duration}
                    onChange={(e) => setDuration(e.target.value)}
                    placeholder="E.g., 2 days, since this morning..."
                    className="w-full px-4 py-3 bg-slate-50 border border-slate-200 rounded-xl focus:ring-2 focus:ring-primary-500 focus:border-primary-500 focus:bg-white transition-all text-slate-700 placeholder-slate-400"
                  />
                </div>
                <div>
                  <label className="block text-sm font-bold text-slate-700 mb-2">Severity Level</label>
                  <div className="grid grid-cols-3 gap-3">
                    {['Mild', 'Moderate', 'Severe'].map((lvl) => (
                      <button
                        key={lvl}
                        type="button"
                        onClick={() => setSeverity(lvl as any)}
                        className={`py-2 px-3 text-sm font-bold rounded-xl border transition-all ${
                          severity === lvl 
                            ? 'bg-primary-50 border-primary-500 text-primary-700 ring-1 ring-primary-500' 
                            : 'bg-white border-slate-200 text-slate-600 hover:border-slate-300 hover:bg-slate-50'
                        }`}
                      >
                        {lvl}
                      </button>
                    ))}
                  </div>
                </div>
              </div>

              <button
                type="submit"
                disabled={isAnalyzing || !symptoms.trim()}
                className="w-full flex justify-center items-center py-4 px-6 rounded-xl shadow-md text-base font-bold text-white bg-primary-600 hover:bg-primary-700 hover:shadow-lg transition-all focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-70 disabled:cursor-not-allowed group"
              >
                {isAnalyzing ? (
                  <>
                    <Loader2 className="animate-spin h-5 w-5 mr-2" />
                    Analyzing Patterns...
                  </>
                ) : (
                  <>
                    Analyze Symptoms <ChevronRight className="w-5 h-5 ml-1 group-hover:translate-x-1 transition-transform" />
                  </>
                )}
              </button>
            </form>
          </div>
        </div>

        {/* Results Area */}
        <div className="lg:col-span-7">
          <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden flex flex-col h-full min-h-[500px]">
            <div className="p-6 border-b border-slate-100 bg-gradient-to-r from-slate-900 to-slate-800 text-white">
              <h3 className="text-lg font-bold flex items-center gap-2">
                <ActivitySquare className="h-5 w-5 text-primary-400" /> AI Diagnostic Insights
              </h3>
            </div>
            
            <div className="p-8 flex-1 flex flex-col relative overflow-hidden">
              {!result && !isAnalyzing && (
                <div className="flex-1 flex flex-col items-center justify-center text-center text-slate-500 h-full">
                  <div className="w-24 h-24 bg-slate-50 rounded-full flex items-center justify-center mb-6 border border-slate-100">
                    <Lightbulb className="h-10 w-10 text-slate-300" />
                  </div>
                  <h3 className="text-xl font-bold text-slate-900 mb-2">Ready to Analyze</h3>
                  <p className="max-w-xs text-sm">Describe your symptoms on the left to see AI-generated wellness insights and recommendations.</p>
                </div>
              )}

              {isAnalyzing && (
                <div className="flex-1 flex flex-col items-center justify-center text-center text-slate-500 h-full relative">
                  <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-64 h-64 bg-primary-100 rounded-full blur-3xl opacity-50 animate-pulse" />
                  <div className="relative z-10 flex flex-col items-center">
                    <Loader2 className="h-12 w-12 text-primary-600 animate-spin mb-6" />
                    <h3 className="text-xl font-bold text-slate-900 mb-2">Cross-referencing symptoms...</h3>
                    <p className="max-w-xs text-sm text-slate-500">Our AI is analyzing your symptoms against thousands of wellness patterns.</p>
                  </div>
                </div>
              )}

              {result && (
                <motion.div 
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="space-y-8"
                >
                  <div className="bg-primary-50 rounded-xl p-6 border border-primary-100">
                    <h4 className="text-xs font-bold text-primary-600 uppercase tracking-widest flex items-center gap-2 mb-3">
                      <Activity className="h-4 w-4" /> AI Analysis
                    </h4>
                    <p className="text-primary-900 text-sm leading-relaxed">{result.insights}</p>
                  </div>

                  <div>
                    <h4 className="text-xs font-bold text-slate-500 uppercase tracking-widest mb-4">Recommended Actions</h4>
                    <div className="grid gap-3">
                      {result.recommendations.map((rec, idx) => (
                        <motion.div 
                          initial={{ opacity: 0, x: -10 }}
                          animate={{ opacity: 1, x: 0 }}
                          transition={{ delay: 0.1 * idx }}
                          key={idx} 
                          className="flex items-start gap-4 p-4 rounded-xl border border-slate-100 bg-white hover:bg-slate-50 transition-colors shadow-sm"
                        >
                          <div className="p-1 bg-green-100 rounded-full shrink-0">
                            <CheckCircle2 className="h-5 w-5 text-green-600" />
                          </div>
                          <p className="text-sm text-slate-700 font-medium leading-relaxed pt-0.5">{rec}</p>
                        </motion.div>
                      ))}
                    </div>
                  </div>

                  {result.seekProfessionalCare && (
                    <motion.div 
                      initial={{ opacity: 0, scale: 0.95 }}
                      animate={{ opacity: 1, scale: 1 }}
                      className="bg-red-50 border-2 border-red-200 rounded-xl p-5 flex items-start gap-4 shadow-sm"
                    >
                      <div className="p-2 bg-red-100 rounded-full shrink-0">
                        <AlertCircle className="h-6 w-6 text-red-600" />
                      </div>
                      <div>
                        <h4 className="text-sm font-bold text-red-900">Professional Care Recommended</h4>
                        <p className="text-sm text-red-700 mt-1 leading-relaxed">Based on the severity and nature of these symptoms, we strongly recommend consulting a healthcare professional immediately.</p>
                      </div>
                    </motion.div>
                  )}
                  
                  <div className="pt-6 mt-8 border-t border-slate-100 flex justify-end">
                    <button 
                      onClick={handleReset} 
                      className="px-6 py-2.5 rounded-lg text-sm font-bold text-slate-600 bg-slate-100 hover:bg-slate-200 transition-colors"
                    >
                      Analyze New Symptoms
                    </button>
                  </div>
                </motion.div>
              )}
            </div>
          </div>
        </div>
      </div>
    </motion.div>
  );
};

export default SymptomAnalyzer;
