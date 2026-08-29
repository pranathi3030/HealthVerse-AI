import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  // Serve project root files (like logo.png) as static assets
  publicDir: 'public',
  server: {
    allowedHosts: true,
    fs: {
      // Allow serving files from the project root
      allow: ['..'],
    },
  },
})
