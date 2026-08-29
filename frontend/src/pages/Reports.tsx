import React, { useState, useRef } from 'react';
import { reportApi } from '../services/reportApi';
import { ReportAnalysis } from '../types/report';
import { motion, AnimatePresence } from 'framer-motion';
import { 
  UploadCloud, File, X, Loader2, AlertCircle, CheckCircle2, 
  AlertTriangle, FileText, Activity, ShieldCheck, ChevronRight
} from 'lucide-react';

const Reports = () => {
  const [file, setFile] = useState<File | null>(null);
  const [error, setError] = useState('');
  const [isDragging, setIsDragging] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [uploadState, setUploadState] = useState<'idle' | 'uploading' | 'analyzing' | 'complete'>('idle');
  const [analysisResult, setAnalysisResult] = useState<ReportAnalysis | null>(null);
  
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleDragOver = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    setIsDragging(false);
    
    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      validateAndSetFile(e.dataTransfer.files[0]);
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      validateAndSetFile(e.target.files[0]);
    }
  };

  const validateAndSetFile = (selectedFile: File) => {
    setError('');
    const validTypes = ['application/pdf', 'image/jpeg', 'image/png'];
    if (!validTypes.includes(selectedFile.type)) {
      setError('Invalid file type. Please upload a PDF, JPG, or PNG.');
      return;
    }
    if (selectedFile.size > 10 * 1024 * 1024) { // 10MB limit
      setError('File is too large. Maximum size is 10MB.');
      return;
    }
    setFile(selectedFile);
  };

  const clearFile = () => {
    setFile(null);
    setUploadProgress(0);
    setUploadState('idle');
    setAnalysisResult(null);
    if (fileInputRef.current) fileInputRef.current.value = '';
  };

  const handleUpload = async () => {
    if (!file) return;
    
    setError('');
    setUploadState('uploading');
    
    try {
      const { id } = await reportApi.uploadReport(file, (progress) => {
        setUploadProgress(progress);
      });
      
      setUploadState('analyzing');
      const analysis = await reportApi.getAnalysis(id);
      
      setAnalysisResult(analysis);
      setUploadState('complete');
    } catch (err) {
      setError('An error occurred during upload or analysis. Please try again.');
      setUploadState('idle');
    }
  };

  return (
    <motion.div 
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      className="max-w-4xl mx-auto space-y-8 pb-12"
    >
      <div className="flex items-center justify-between bg-white p-6 rounded-2xl shadow-sm border border-slate-100">
        <div>
          <h1 className="text-3xl font-extrabold text-slate-900 flex items-center gap-3">
            <div className="p-2 bg-secondary-50 rounded-xl">
              <FileText className="h-8 w-8 text-secondary-600" />
            </div>
            Medical Reports
          </h1>
          <p className="text-slate-500 mt-2">Upload your blood tests or medical documents for instant AI wellness analysis.</p>
        </div>
      </div>

      <AnimatePresence>
        {error && (
          <motion.div 
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-xl flex items-center gap-2 shadow-sm"
          >
            <AlertCircle className="h-5 w-5 flex-shrink-0" />
            <span className="text-sm font-medium">{error}</span>
          </motion.div>
        )}
      </AnimatePresence>

      {/* Upload Section */}
      {uploadState === 'idle' && (
        <motion.div 
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          className="bg-white rounded-2xl shadow-sm border border-slate-100 p-8"
        >
          {!file ? (
            <div 
              onDragOver={handleDragOver}
              onDragLeave={handleDragLeave}
              onDrop={handleDrop}
              onClick={() => fileInputRef.current?.click()}
              className={`relative border-2 border-dashed rounded-2xl p-16 text-center transition-all duration-300 cursor-pointer overflow-hidden group
                ${isDragging ? 'border-primary-500 bg-primary-50' : 'border-slate-200 hover:border-primary-400 hover:bg-slate-50'}`}
            >
              <div className={`absolute inset-0 bg-primary-500/5 transition-opacity duration-300 ${isDragging ? 'opacity-100' : 'opacity-0'}`} />
              
              <div className="relative z-10 flex flex-col items-center">
                <motion.div 
                  animate={{ y: isDragging ? -10 : 0, scale: isDragging ? 1.1 : 1 }}
                  className={`w-20 h-20 rounded-full flex items-center justify-center mb-6 transition-colors duration-300
                    ${isDragging ? 'bg-primary-100 text-primary-600' : 'bg-slate-100 text-slate-400 group-hover:bg-primary-50 group-hover:text-primary-500'}`}
                >
                  <UploadCloud className="h-10 w-10" />
                </motion.div>
                <h3 className="text-xl font-bold text-slate-900 mb-2">Drag & drop your report here</h3>
                <p className="text-sm font-medium text-slate-500 mb-6">or click to browse your computer</p>
                <div className="flex items-center gap-2 text-xs font-semibold text-slate-400 uppercase tracking-widest bg-white px-4 py-2 rounded-full shadow-sm border border-slate-100">
                  <span>PDF</span> <span className="w-1 h-1 rounded-full bg-slate-300" />
                  <span>JPG</span> <span className="w-1 h-1 rounded-full bg-slate-300" />
                  <span>PNG</span> <span className="w-1 h-1 rounded-full bg-slate-300" />
                  <span>Max 10MB</span>
                </div>
              </div>
              
              <input 
                type="file" 
                className="hidden" 
                ref={fileInputRef}
                accept=".pdf,image/jpeg,image/png"
                onChange={handleFileChange}
              />
            </div>
          ) : (
            <motion.div 
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="space-y-6"
            >
              <div className="flex items-center justify-between p-6 bg-slate-50 border border-slate-200 rounded-xl relative overflow-hidden">
                <div className="absolute left-0 top-0 bottom-0 w-1.5 bg-secondary-500" />
                <div className="flex items-center gap-5">
                  <div className="h-14 w-14 bg-white rounded-xl shadow-sm flex items-center justify-center border border-slate-100 text-secondary-500">
                    <File className="h-7 w-7" />
                  </div>
                  <div>
                    <p className="text-base font-bold text-slate-900">{file.name}</p>
                    <p className="text-sm text-slate-500 mt-0.5">{(file.size / 1024 / 1024).toFixed(2)} MB</p>
                  </div>
                </div>
                <button onClick={clearFile} className="w-10 h-10 rounded-full bg-white border border-slate-200 flex items-center justify-center text-slate-400 hover:text-red-500 hover:border-red-200 hover:bg-red-50 transition-all">
                  <X className="h-5 w-5" />
                </button>
              </div>
              <div className="flex justify-end gap-4">
                <button onClick={clearFile} className="px-6 py-3 text-sm font-bold text-slate-600 bg-white border border-slate-200 rounded-xl hover:bg-slate-50 transition-colors">
                  Cancel
                </button>
                <button onClick={handleUpload} className="px-6 py-3 text-sm font-bold text-white bg-primary-600 rounded-xl hover:bg-primary-700 shadow-md hover:shadow-lg transition-all flex items-center gap-2">
                  <Activity className="w-4 h-4" /> Analyze Report
                </button>
              </div>
            </motion.div>
          )}
        </motion.div>
      )}

      {/* Loading States */}
      {(uploadState === 'uploading' || uploadState === 'analyzing') && (
        <motion.div 
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          className="bg-white rounded-2xl shadow-sm border border-slate-100 p-16 text-center relative overflow-hidden"
        >
          {/* Subtle background pulse */}
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-64 h-64 bg-primary-100 rounded-full blur-3xl opacity-50 animate-pulse" />
          
          <div className="relative z-10 flex flex-col items-center">
            <div className="w-20 h-20 rounded-2xl bg-primary-50 flex items-center justify-center mb-6">
              <Loader2 className="h-10 w-10 text-primary-600 animate-spin" />
            </div>
            <h3 className="text-2xl font-bold text-slate-900 mb-2">
              {uploadState === 'uploading' ? 'Uploading report...' : 'AI is analyzing your report...'}
            </h3>
            <p className="text-base text-slate-500 mb-8 max-w-md mx-auto">
              {uploadState === 'uploading' 
                ? 'Securely transferring to our encrypted servers.' 
                : 'Extracting medical markers and generating a personalized wellness summary.'}
            </p>
            
            {uploadState === 'uploading' && (
              <div className="w-full max-w-md mx-auto">
                <div className="flex justify-between text-xs font-bold text-slate-500 mb-2 uppercase tracking-wider">
                  <span>Uploading</span>
                  <span>{uploadProgress}%</span>
                </div>
                <div className="w-full bg-slate-100 rounded-full h-3 p-0.5">
                  <div className="bg-primary-600 h-full rounded-full transition-all duration-300" style={{ width: `${uploadProgress}%` }}></div>
                </div>
              </div>
            )}
          </div>
        </motion.div>
      )}

      {/* Analysis Result */}
      {uploadState === 'complete' && analysisResult && (
        <motion.div 
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="space-y-8"
        >
          <div className="bg-white rounded-2xl shadow-sm border border-slate-100 overflow-hidden">
            <div className="p-8 border-b border-slate-100 bg-gradient-to-r from-slate-900 to-slate-800 text-white flex justify-between items-center">
              <div>
                <div className="flex items-center gap-2 mb-2">
                  <ShieldCheck className="w-5 h-5 text-green-400" />
                  <span className="text-xs font-bold text-green-400 uppercase tracking-widest">Analysis Complete</span>
                </div>
                <h3 className="text-2xl font-bold">{analysisResult.reportTitle}</h3>
                <p className="text-slate-400 text-sm mt-1">Analyzed on {analysisResult.dateAnalyzed}</p>
              </div>
              <button onClick={clearFile} className="px-4 py-2 bg-white/10 hover:bg-white/20 text-white text-sm font-medium rounded-lg transition-colors border border-white/10 backdrop-blur-sm">
                Upload New
              </button>
            </div>
            
            <div className="p-8 space-y-10">
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                {/* Summary */}
                <div className="space-y-4">
                  <h4 className="text-xs font-bold text-slate-500 uppercase tracking-widest flex items-center gap-2">
                    <FileText className="h-4 w-4" /> Medical Summary
                  </h4>
                  <div className="bg-slate-50 p-5 rounded-xl border border-slate-100 text-slate-700 text-sm leading-relaxed h-full">
                    {analysisResult.summary}
                  </div>
                </div>

                {/* Simple Explanation */}
                <div className="space-y-4">
                  <h4 className="text-xs font-bold text-primary-600 uppercase tracking-widest flex items-center gap-2">
                    <Activity className="h-4 w-4" /> In Simple Terms
                  </h4>
                  <div className="bg-primary-50 p-5 rounded-xl border border-primary-100 text-primary-900 text-sm leading-relaxed h-full">
                    {analysisResult.simpleExplanation}
                  </div>
                </div>
              </div>

              {/* Observations Table */}
              <div>
                <h4 className="text-xs font-bold text-slate-500 uppercase tracking-widest mb-6">Key Biomarker Observations</h4>
                <div className="border border-slate-200 rounded-xl overflow-hidden shadow-sm">
                  <table className="min-w-full divide-y divide-slate-200">
                    <thead className="bg-slate-50">
                      <tr>
                        <th className="px-6 py-4 text-left text-xs font-bold text-slate-500 uppercase tracking-wider">Marker</th>
                        <th className="px-6 py-4 text-left text-xs font-bold text-slate-500 uppercase tracking-wider">Value</th>
                        <th className="px-6 py-4 text-left text-xs font-bold text-slate-500 uppercase tracking-wider">Status</th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-slate-100">
                      {analysisResult.observations.map((obs, idx) => (
                        <motion.tr 
                          initial={{ opacity: 0, x: -10 }}
                          animate={{ opacity: 1, x: 0 }}
                          transition={{ delay: 0.1 * idx }}
                          key={idx} 
                          className="hover:bg-slate-50 transition-colors"
                        >
                          <td className="px-6 py-4">
                            <p className="text-sm font-bold text-slate-900">{obs.name}</p>
                            {obs.description && <p className="text-xs text-slate-500 mt-1">{obs.description}</p>}
                          </td>
                          <td className="px-6 py-4 text-sm font-bold text-slate-900 whitespace-nowrap">
                            {obs.value}
                          </td>
                          <td className="px-6 py-4 whitespace-nowrap">
                            <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-bold ${
                              obs.status === 'normal' ? 'bg-green-100 text-green-700' :
                              obs.status === 'attention' ? 'bg-amber-100 text-amber-700' : 'bg-red-100 text-red-700'
                            }`}>
                              {obs.status === 'attention' && <AlertTriangle className="h-3 w-3 mr-1.5" />}
                              {obs.status.charAt(0).toUpperCase() + obs.status.slice(1)}
                            </span>
                          </td>
                        </motion.tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>

              {/* Recommendations */}
              <div>
                <h4 className="text-xs font-bold text-slate-500 uppercase tracking-widest mb-6">Actionable Recommendations</h4>
                <div className="grid gap-4">
                  {analysisResult.recommendations.map((rec, idx) => (
                    <motion.div 
                      initial={{ opacity: 0, y: 10 }}
                      animate={{ opacity: 1, y: 0 }}
                      transition={{ delay: 0.3 + (0.1 * idx) }}
                      key={idx} 
                      className="flex items-start gap-4 p-4 rounded-xl border border-slate-100 bg-white shadow-sm hover:shadow-md transition-shadow"
                    >
                      <div className="h-8 w-8 rounded-full bg-primary-50 flex items-center justify-center text-sm font-bold text-primary-600 shrink-0">
                        {idx + 1}
                      </div>
                      <p className="text-sm text-slate-700 font-medium leading-relaxed pt-1">{rec}</p>
                    </motion.div>
                  ))}
                </div>
              </div>

              {/* Disclaimer */}
              {analysisResult.seekProfessionalCare ? (
                <div className="bg-red-50 border-2 border-red-200 rounded-xl p-6 flex items-start gap-4">
                  <div className="p-2 bg-red-100 rounded-full shrink-0">
                    <AlertCircle className="h-6 w-6 text-red-600" />
                  </div>
                  <div>
                    <h4 className="text-base font-bold text-red-900">Professional Care Recommended</h4>
                    <p className="text-sm font-medium text-red-700 mt-2 leading-relaxed">{analysisResult.professionalCareMessage || 'Based on these results, we strongly advise consulting with a healthcare professional.'}</p>
                  </div>
                </div>
              ) : (
                <div className="bg-slate-50 rounded-xl p-4 flex items-center justify-center gap-2 text-slate-500">
                  <AlertCircle className="w-4 h-4" />
                  <p className="text-xs font-medium">This AI analysis provides wellness guidance and does not replace a professional medical diagnosis.</p>
                </div>
              )}
            </div>
          </div>
        </motion.div>
      )}
    </motion.div>
  );
};

export default Reports;
