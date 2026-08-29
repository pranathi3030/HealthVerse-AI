import React from 'react';
import { MedicationReminder } from '../../mock-data/dashboardMocks';
import { Pill } from 'lucide-react';

interface MedicationRemindersProps {
  reminders: MedicationReminder[];
}

export const MedicationReminders: React.FC<MedicationRemindersProps> = ({ reminders }) => {
  return (
    <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
      <h3 className="text-lg font-medium text-slate-900 mb-4">Medication Reminders</h3>
      <div className="space-y-3">
        {reminders.map((reminder) => (
          <div key={reminder.id} className="flex items-center justify-between p-3 bg-slate-50 rounded-lg border border-slate-100">
            <div className="flex items-center gap-3">
              <div className="h-8 w-8 rounded-full bg-white flex items-center justify-center border border-slate-200">
                <Pill className="h-4 w-4 text-primary-500" />
              </div>
              <div>
                <p className="text-sm font-medium text-slate-900">{reminder.name}</p>
                <p className="text-xs text-slate-500">
                  {reminder.status === 'taken' ? `Taken at ${reminder.time}` : `Scheduled for ${reminder.time}`}
                </p>
              </div>
            </div>
            <div className={`h-3 w-3 rounded-full ${
              reminder.status === 'taken' ? 'bg-green-500' :
              reminder.status === 'scheduled' ? 'bg-amber-500' : 'bg-red-500'
            }`} />
          </div>
        ))}
      </div>
    </div>
  );
};
