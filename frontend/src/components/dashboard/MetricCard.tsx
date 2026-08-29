import React from 'react';
import { Metric } from '../../mock-data/dashboardMocks';
import { LucideIcon, TrendingUp, TrendingDown, Minus } from 'lucide-react';

interface MetricCardProps {
  title: string;
  metric: Metric;
  icon: LucideIcon;
  colorClass: string;
  bgClass: string;
}

export const MetricCard: React.FC<MetricCardProps> = ({ title, metric, icon: Icon, colorClass, bgClass }) => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-6 flex flex-col justify-between">
      <div className="flex items-center justify-between mb-4">
        <div className="flex items-center gap-3">
          <div className={`h-10 w-10 rounded-full flex items-center justify-center ${bgClass}`}>
            <Icon className={`h-5 w-5 ${colorClass}`} />
          </div>
          <h2 className="text-sm font-medium text-slate-500">{title}</h2>
        </div>
        {metric.status && (
          <div className={`h-2.5 w-2.5 rounded-full ${
            metric.status === 'good' ? 'bg-green-500' : 
            metric.status === 'warning' ? 'bg-amber-500' : 'bg-red-500'
          }`} />
        )}
      </div>
      
      <div className="flex items-end justify-between">
        <div className="flex items-baseline gap-1">
          <p className="text-2xl font-semibold text-slate-900">{metric.value}</p>
          {metric.unit && <span className="text-sm text-slate-500">{metric.unit}</span>}
        </div>
        
        {metric.trend && metric.trendValue && (
          <div className={`flex items-center text-xs font-medium ${
            metric.trend === 'up' && metric.status === 'good' ? 'text-green-600' :
            metric.trend === 'down' && metric.status === 'good' ? 'text-green-600' :
            metric.trend === 'up' && metric.status !== 'good' ? 'text-amber-600' :
            metric.trend === 'down' && metric.status !== 'good' ? 'text-amber-600' :
            'text-slate-500'
          }`}>
            {metric.trend === 'up' && <TrendingUp className="h-3 w-3 mr-1" />}
            {metric.trend === 'down' && <TrendingDown className="h-3 w-3 mr-1" />}
            {metric.trend === 'stable' && <Minus className="h-3 w-3 mr-1" />}
            {metric.trendValue}
          </div>
        )}
      </div>
    </div>
  );
};
