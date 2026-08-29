import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { healthApi } from '../services/healthApi';
import { HealthProfile as HealthProfileType, defaultHealthProfile } from '../types/health';
import { Loader2, Save, CheckCircle2, AlertCircle } from 'lucide-react';

const HealthProfile = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState<HealthProfileType>(defaultHealthProfile);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');
  const [errorMsg, setErrorMsg] = useState('');

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        if (!user) return;
        const data = await healthApi.getProfile(user.id);
        setProfile(data);
      } catch (err) {
        setErrorMsg('Failed to load profile data.');
      } finally {
        setIsLoading(false);
      }
    };
    fetchProfile();
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setProfile((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSuccessMsg('');
    setErrorMsg('');
    setIsSaving(true);
    
    // Basic validation
    if (!profile.age || !profile.height || !profile.weight) {
      setErrorMsg('Please fill out all required numeric fields (Age, Height, Weight).');
      setIsSaving(false);
      return;
    }

    try {
      const updatedProfile = await healthApi.updateProfile(profile);
      setProfile(updatedProfile);
      setSuccessMsg('Health profile updated successfully.');
      setTimeout(() => setSuccessMsg(''), 5000);
    } catch (err) {
      setErrorMsg('Failed to update profile. Please try again.');
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <Loader2 className="h-8 w-8 text-primary-600 animate-spin" />
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Health Profile</h1>
          <p className="text-slate-500 mt-1">Manage your personal health data to improve AI recommendations.</p>
        </div>
      </div>

      {successMsg && (
        <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg flex items-center gap-2">
          <CheckCircle2 className="h-5 w-5" />
          {successMsg}
        </div>
      )}
      
      {errorMsg && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg flex items-center gap-2">
          <AlertCircle className="h-5 w-5" />
          {errorMsg}
        </div>
      )}

      <div className="bg-white rounded-xl shadow-sm border border-slate-100 overflow-hidden">
        <form onSubmit={handleSubmit} className="divide-y divide-slate-100">
          
          <div className="p-6 md:p-8 space-y-8">
            {/* Basic Information Section */}
            <div>
              <h3 className="text-lg font-medium text-slate-900 mb-4 border-b border-slate-100 pb-2">Basic Information</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Age</label>
                  <input type="number" name="age" value={profile.age} onChange={handleChange} required min="1" max="120"
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Gender</label>
                  <select name="gender" value={profile.gender} onChange={handleChange} required
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500">
                    <option value="" disabled>Select Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Other">Other</option>
                    <option value="Prefer not to say">Prefer not to say</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Height (cm)</label>
                  <input type="number" name="height" value={profile.height} onChange={handleChange} required min="50" max="300"
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Weight (kg)</label>
                  <input type="number" name="weight" value={profile.weight} onChange={handleChange} required min="20" max="500"
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500" />
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Blood Group</label>
                  <select name="bloodGroup" value={profile.bloodGroup} onChange={handleChange}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500">
                    <option value="" disabled>Select Blood Group</option>
                    <option value="A+">A+</option><option value="A-">A-</option>
                    <option value="B+">B+</option><option value="B-">B-</option>
                    <option value="O+">O+</option><option value="O-">O-</option>
                    <option value="AB+">AB+</option><option value="AB-">AB-</option>
                  </select>
                </div>
              </div>
            </div>

            {/* Medical History Section */}
            <div>
              <h3 className="text-lg font-medium text-slate-900 mb-4 border-b border-slate-100 pb-2">Medical History</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-slate-700 mb-1">Allergies</label>
                  <textarea name="allergies" value={profile.allergies} onChange={handleChange} rows={2} placeholder="e.g. Peanuts, Penicillin (Leave empty or 'None' if none)"
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500" />
                </div>
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-slate-700 mb-1">Chronic Conditions</label>
                  <textarea name="chronicConditions" value={profile.chronicConditions} onChange={handleChange} rows={2} placeholder="e.g. Asthma, Hypertension (Leave empty or 'None' if none)"
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500" />
                </div>
                <div className="md:col-span-2">
                  <label className="block text-sm font-medium text-slate-700 mb-1">Current Medications</label>
                  <textarea name="currentMedications" value={profile.currentMedications} onChange={handleChange} rows={2} placeholder="List current medications and dosages"
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500" />
                </div>
              </div>
            </div>

            {/* Lifestyle Section */}
            <div>
              <h3 className="text-lg font-medium text-slate-900 mb-4 border-b border-slate-100 pb-2">Lifestyle & Goals</h3>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Lifestyle / Activity Level</label>
                  <select name="lifestyle" value={profile.lifestyle} onChange={handleChange}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500">
                    <option value="" disabled>Select Activity Level</option>
                    <option value="Sedentary">Sedentary (Little to no exercise)</option>
                    <option value="Lightly Active">Lightly Active (Light exercise 1-3 days/week)</option>
                    <option value="Moderately Active">Moderately Active (Moderate exercise 3-5 days/week)</option>
                    <option value="Very Active">Very Active (Hard exercise 6-7 days/week)</option>
                    <option value="Super Active">Super Active (Very hard exercise & physical job)</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Fitness Goal</label>
                  <select name="fitnessGoal" value={profile.fitnessGoal} onChange={handleChange}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500">
                    <option value="" disabled>Select Goal</option>
                    <option value="Weight Loss">Weight Loss</option>
                    <option value="Maintenance">Maintenance</option>
                    <option value="Muscle Gain">Muscle Gain</option>
                    <option value="Endurance">Endurance / Stamina</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Average Sleep Pattern</label>
                  <select name="sleepPattern" value={profile.sleepPattern} onChange={handleChange}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500">
                    <option value="" disabled>Select Sleep Pattern</option>
                    <option value="Less than 5 hours">Less than 5 hours</option>
                    <option value="5-7 hours">5-7 hours</option>
                    <option value="7-9 hours">7-9 hours</option>
                    <option value="More than 9 hours">More than 9 hours</option>
                  </select>
                </div>
                <div>
                  <label className="block text-sm font-medium text-slate-700 mb-1">Dietary Preference</label>
                  <select name="dietaryPreference" value={profile.dietaryPreference} onChange={handleChange}
                    className="w-full px-3 py-2 border border-slate-300 rounded-md focus:ring-primary-500 focus:border-primary-500">
                    <option value="" disabled>Select Diet</option>
                    <option value="None">No Specific Diet</option>
                    <option value="Vegetarian">Vegetarian</option>
                    <option value="Vegan">Vegan</option>
                    <option value="Keto">Keto</option>
                    <option value="Paleo">Paleo</option>
                    <option value="Mediterranean">Mediterranean</option>
                  </select>
                </div>
              </div>
            </div>
          </div>

          <div className="px-6 py-4 bg-slate-50 flex items-center justify-end gap-4 border-t border-slate-100">
            <button type="button" onClick={() => window.location.reload()} disabled={isSaving} className="text-sm font-medium text-slate-600 hover:text-slate-900">
              Reset Changes
            </button>
            <button
              type="submit"
              disabled={isSaving}
              className="inline-flex items-center justify-center px-6 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 disabled:opacity-70 disabled:cursor-not-allowed"
            >
              {isSaving ? (
                <><Loader2 className="animate-spin h-4 w-4 mr-2" /> Saving...</>
              ) : (
                <><Save className="h-4 w-4 mr-2" /> Save Profile</>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default HealthProfile;
