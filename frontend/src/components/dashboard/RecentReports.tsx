import React from 'react';
import { Report } from '../../mock-data/dashboardMocks';
import { FileText, ChevronRight, Loader2, CheckCircle } from 'lucide-react';
import { Link } from 'react-router-dom';

interface RecentReportsProps {
  reports: Report[];
}

export const RecentReports: React.FC<RecentReportsProps> = ({ reports }) => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
      <div className="flex items-center justify-between mb-4">
        <h3 className="text-lg font-medium text-slate-900">Recent Reports</h3>
        <Link to="/reports" className="text-sm font-medium text-primary-600 hover:text-primary-700 flex items-center">
          View all <ChevronRight className="h-4 w-4 ml-1" />
        </Link>
      </div>
      <div className="space-y-4">
        {reports.map((report) => (
          <div key={report.id} className="flex items-start p-4 bg-slate-50 rounded-lg border border-slate-100">
            <div className="h-10 w-10 rounded-full bg-white flex items-center justify-center border border-slate-200 flex-shrink-0">
              <FileText className="h-5 w-5 text-slate-500" />
            </div>
            <div className="ml-4 flex-1">
              <h4 className="text-sm font-medium text-slate-900">{report.title}</h4>
              <p className="text-xs text-slate-500 mb-1">{report.date}</p>
              {report.summary && <p className="text-xs text-slate-700">{report.summary}</p>}
            </div>
            <div>
              {report.status === 'processing' ? (
                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800">
                  <Loader2 className="h-3 w-3 mr-1 animate-spin" /> Processing
                </span>
              ) : (
                <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                  <CheckCircle className="h-3 w-3 mr-1" /> Analyzed
                </span>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
