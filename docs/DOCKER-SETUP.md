# Docker + Selenium Grid Setup

## Overview

This framework supports running tests on **Selenium Grid** using Docker containers for scalable, isolated test execution.

### Architecture

```
┌─────────────────────────────────────────────────────┐
│                  Test Execution                      │
│                                                      │
│  mvn test -Dexecution=grid -Dbrowser=chrome         │
└──────────────────┬──────────────────────────────────┘
                   │
                   ▼
         ┌─────────────────────┐
         │   Selenium Hub      │
         │   localhost:4444    │
         └─────────┬───────────┘
                   │
         ┌─────────┴─────────┐
         │                   │
    ┌────▼────┐         ┌────▼────┐
    │ Chrome  │         │ Firefox │
    │  Node   │         │  Node   │
    │         │         │         │
    │ Port:   │         │ Port:   │
    │  5900   │         │  5901   │
    └─────────┘         └─────────┘
```

---

## Prerequisites

- **Docker Desktop** installed and running
- **Docker Compose** (included with Docker Desktop)

---

## Quick Start

### 1. Start Selenium Grid

```bash
# From project root
docker-compose up -d
```

**What this does:**
- Starts Selenium Hub on port 4444
- Starts Chrome node (max 2 sessions)
- Starts Firefox node (max 2 sessions)
- Enables VNC for visual debugging

### 2. Verify Grid is Running

**Option A: Browser**
- Open: http://localhost:4444/ui
- Should see Hub console with 2 nodes registered

**Option B: Command line**
```bash
docker-compose ps
```

Expected output:
```
NAME                IMAGE                        STATUS
selenium-hub        selenium/hub:4.27.0         Up
selenium-chrome     selenium/node-chrome:4.27.0  Up
selenium-firefox    selenium/node-firefox:4.27.0 Up
```

### 3. Run Tests on Grid

```bash
# Chrome on Grid
mvn test -Dexecution=grid -Dbrowser=chrome

# Firefox on Grid
mvn test -Dexecution=grid -Dbrowser=firefox

# Specific test group
mvn test -Dexecution=grid -Dgroups=smoke
```

### 4. Stop Grid

```bash
docker-compose down
```

---

## Configuration

### System Properties

| Property | Default | Description |
|----------|---------|-------------|
| `execution` | `local` | Execution mode: `local` or `grid` |
| `browser` | `chrome` | Browser: `chrome` or `firefox` |
| `grid.url` | `http://localhost:4444` | Grid Hub URL |

### Examples

```bash
# Remote Grid
mvn test -Dexecution=grid -Dgrid.url=http://192.168.1.100:4444

# Firefox on remote Grid
mvn test -Dexecution=grid -Dbrowser=firefox -Dgrid.url=http://grid-server:4444
```

---

## Visual Debugging with VNC

Grid nodes expose VNC for watching tests execute in real-time.

### Access VNC

**Chrome Node:**
- VNC Port: `5900`
- VNC Viewer: `localhost:5900`
- Password: `secret`

**Firefox Node:**
- VNC Port: `5901`
- VNC Viewer: `localhost:5901`
- Password: `secret`

### VNC Clients

- **Windows:** [TightVNC](https://www.tightvnc.com/download.php)
- **macOS:** Built-in Screen Sharing or [RealVNC](https://www.realvnc.com/en/connect/download/viewer/)
- **Linux:** `vinagre` or `remmina`

---

## Troubleshooting

### Grid not starting

**Issue:** `Cannot connect to the Docker daemon`

**Solution:**
```bash
# Ensure Docker Desktop is running
# Windows: Check system tray for Docker icon
# macOS: Check menu bar for Docker icon
```

---

### Tests failing with connection error

**Error:** `Failed to connect to Selenium Grid`

**Check:**
```bash
# Verify Grid is running
docker-compose ps

# Check Hub logs
docker logs selenium-hub

# Verify Hub is accessible
curl http://localhost:4444/status
```

---

### Node not registering with Hub

**Check logs:**
```bash
# Chrome node
docker logs selenium-chrome

# Firefox node
docker logs selenium-firefox
```

**Common fix:**
```bash
# Restart Grid
docker-compose restart
```

---

### Out of memory errors

**Issue:** Browser crashes or OOM errors

**Solution:** Increase shared memory in `docker-compose.yml`:
```yaml
chrome:
  shm_size: '4gb'  # Increase from 2gb
```

---

## CI/CD Integration

### GitHub Actions Example

```yaml
jobs:
  test-grid:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Start Selenium Grid
        run: docker-compose up -d

      - name: Wait for Grid
        run: |
          timeout 60 sh -c 'until curl -f http://localhost:4444/status; do sleep 2; done'

      - name: Run tests
        run: mvn test -Dexecution=grid

      - name: Stop Grid
        if: always()
        run: docker-compose down
```

---

## Performance Tuning

### Parallel Sessions

Increase max sessions per node in `docker-compose.yml`:

```yaml
chrome:
  environment:
    - SE_NODE_MAX_SESSIONS=4  # Default: 2
```

### Multiple Nodes

Scale nodes horizontally:

```bash
# 3 Chrome nodes
docker-compose up -d --scale chrome=3

# 2 Firefox nodes
docker-compose up -d --scale firefox=2
```

---

## Resource Management

### View Resource Usage

```bash
docker stats
```

### Clean Up Old Containers

```bash
# Remove stopped containers
docker-compose down --remove-orphans

# Remove all unused resources
docker system prune -a
```

---

## Best Practices

1. **Always stop Grid when not in use** - Containers consume resources
2. **Use Grid for parallel execution** - Run multiple browsers simultaneously
3. **VNC for debugging only** - Adds overhead, disable in CI
4. **Monitor resource usage** - Especially with multiple nodes
5. **Keep images updated** - `docker-compose pull` regularly

---

## References

- [Selenium Grid Docs](https://www.selenium.dev/documentation/grid/)
- [Docker Hub - Selenium](https://hub.docker.com/u/selenium)
- [Docker Compose Docs](https://docs.docker.com/compose/)
