import React from 'react';

interface WellnessIndicator {
  label: string;
  score: number;
  max: number;
}

interface WellnessIndicatorsProps {
  indicators: WellnessIndicator[];
}

export const WellnessIndicators: React.FC<WellnessIndicatorsProps> = ({ indicators }) => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
      <h3 className="text-lg font-medium text-slate-900 mb-6">Wellness Risk Indicators</h3>
      <div className="space-y-6">
        {indicators.map((indicator, index) => {
          const percentage = (indicator.score / indicator.max) * 100;
          let colorClass = 'bg-green-500';
          if (percentage < 60) colorClass = 'bg-red-500';
          else if (percentage < 80) colorClass = 'bg-amber-500';

          return (
            <div key={index}>
              <div className="flex justify-between items-end mb-1">
                <span className="text-sm font-medium text-slate-700">{indicator.label}</span>
                <span className="text-sm font-semibold text-slate-900">{indicator.score}/{indicator.max}</span>
              </div>
              <div className="w-full bg-slate-100 rounded-full h-2.5">
                <div className={`${colorClass} h-2.5 rounded-full`} style={{ width: `${percentage}%` }}></div>
              </div>
            </div>
          );
        })}
      </div>
      <div className="mt-6 bg-primary-50 rounded-lg p-4 border border-primary-100">
        <p className="text-primary-800 text-sm leading-relaxed">
          <span className="font-semibold block mb-1">AI Recommendation:</span>
          Increase daily activity and maintain consistent sleep to improve your Recovery score.
        </p>
      </div>
    </div>
  );
};
