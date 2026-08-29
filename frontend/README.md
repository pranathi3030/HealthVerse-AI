# HealthVerse AI — Frontend

React + TypeScript + Vite frontend for the HealthVerse AI healthcare platform.

## Prerequisites

- Node.js 20+
- npm 10+

## Development

```bash
npm install
npm run dev
```

## Environment Variables

Copy `.env.example` to `.env` and configure:

```
VITE_API_BASE_URL=http://localhost:8080
```

> **Note:** `VITE_*` variables are embedded at build time by Vite. They are exposed to the browser — never put private secrets here.

---

## Docker

### Build

```bash
docker build -t healthverse-frontend .
```

To specify a custom API URL at build time:

```bash
docker build -t healthverse-frontend --build-arg VITE_API_BASE_URL=http://your-api-host:8080 .
```

### Run

```bash
docker run --name healthverse-frontend -p 3000:80 healthverse-frontend
```

### Open Application

[http://localhost:3000](http://localhost:3000)

### Stop & Remove

```bash
docker stop healthverse-frontend
docker rm healthverse-frontend
```

---

## Docker Compose Integration (for Member 2)

When integrating with the backend via Docker Compose:

- The frontend runs in the **browser**, not inside Docker at runtime
- `VITE_API_BASE_URL` must be a URL accessible from the **user's browser**, not a Docker internal hostname
- Use `http://localhost:<port>` if the backend is port-mapped to the host
- Or configure an Nginx reverse proxy to forward `/api` requests to the backend service

Example Docker Compose snippet:

```yaml
services:
  frontend:
    build:
      context: ./frontend
      args:
        VITE_API_BASE_URL: http://localhost:8080
    ports:
      - "3000:80"
```
