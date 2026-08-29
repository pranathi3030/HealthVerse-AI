import React from 'react';
import { WellnessPlanItem } from '../../mock-data/dashboardMocks';
import { CheckCircle2, Circle } from 'lucide-react';

interface WellnessPlanProps {
  plan: WellnessPlanItem[];
}

export const WellnessPlan: React.FC<WellnessPlanProps> = ({ plan }) => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
      <h3 className="text-lg font-medium text-slate-900 mb-4">Today's Wellness Plan</h3>
      <div className="space-y-3">
        {plan.map((item) => (
          <div key={item.id} className="flex items-center justify-between p-3 border-b border-slate-100 last:border-0 last:pb-0">
            <div className="flex items-center gap-3">
              {item.completed ? (
                <CheckCircle2 className="h-5 w-5 text-green-500" />
              ) : (
                <Circle className="h-5 w-5 text-slate-300" />
              )}
              <div className={item.completed ? "line-through text-slate-400" : ""}>
                <p className="text-sm font-medium text-slate-900">{item.activity}</p>
              </div>
            </div>
            <span className="text-xs text-slate-500 font-medium bg-slate-100 px-2 py-1 rounded-md">{item.time}</span>
          </div>
        ))}
      </div>
    </div>
  );
};
