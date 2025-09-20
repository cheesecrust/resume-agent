# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Korean AI-powered resume writing assistant (자소서 AI) built with Next.js. The application helps users create professional resumes by improving their draft content using AI models. It features a clean, modern interface with dark theme and Korean localization.

## Common Development Commands

All commands should be run from the `frontend/` directory:

```bash
# Development
cd frontend
npm run dev          # Start development server (Next.js)
npm run build        # Build for production
npm start           # Start production server
npm run lint        # Run ESLint

# Package management
npm install         # Install dependencies
```

## Architecture Overview

### Frontend Structure (Next.js 14 App Router)
- **App Router**: Uses Next.js 14 with app directory structure
- **Styling**: Tailwind CSS v4 with shadcn/ui components
- **Components**: Radix UI primitives with custom styling
- **Theme**: Dark mode by default with next-themes
- **Typography**: Geist font family (sans and mono)
- **Analytics**: Vercel Analytics integration

### Key Directories
- `app/` - Next.js app router pages and API routes
  - `page.tsx` - Homepage with hero section and features
  - `write/page.tsx` - Main form interface for resume writing
  - `api/generate-resume/route.ts` - API endpoint for AI processing
- `components/` - React components
  - `ui/` - shadcn/ui component library
  - `theme-provider.tsx` - Theme context provider
- `lib/` - Utility functions
  - `utils.ts` - Tailwind CSS class utilities
- `hooks/` - Custom React hooks
  - `use-mobile.ts` - Mobile device detection
  - `use-toast.ts` - Toast notification system

### Technology Stack
- **Framework**: Next.js 14 with App Router
- **Language**: TypeScript with strict mode
- **Styling**: Tailwind CSS v4 + shadcn/ui components
- **Forms**: react-hook-form with @hookform/resolvers
- **Validation**: Zod schema validation
- **UI Components**: Radix UI primitives
- **Icons**: Lucide React
- **Charts**: Recharts (for potential data visualization)
- **Notifications**: Sonner toast library

### Configuration Files
- `components.json` - shadcn/ui configuration (New York style, RSC enabled)
- `next.config.mjs` - Next.js config with ESLint/TypeScript build error ignoring
- `tsconfig.json` - TypeScript config with path aliases (`@/*` pointing to `./`)
- `package.json` - Dependencies and scripts

### API Integration
The frontend communicates with a backend server through:
- `/api/generate-resume` - POST endpoint that forwards requests to backend
- Backend URL is configured as `YOUR_BACKEND_SERVER_URL` (needs environment setup)
- Supports multiple AI models (GPT-4 default)

### Styling System
- Uses Tailwind CSS v4 with CSS variables for theming
- Custom utility classes and animations via tailwindcss-animate
- Dark theme as default with neutral base colors
- Responsive design with mobile-first approach

### Form Data Structure
The main form captures:
- `question` - Resume question/prompt
- `draft` - User's initial draft
- `wordLimit` - Character/word limit
- `company` - Target company name
- `position` - Target job position
- `aiModel` - Selected AI model (GPT-4, etc.)

## Development Notes

- TypeScript errors and ESLint warnings are ignored during builds (configured in next.config.mjs)
- Images are unoptimized (configured for static export compatibility)
- Korean language support with proper locale settings
- All components use TypeScript with proper type definitions
- Uses client-side rendering for interactive components ("use client" directive)