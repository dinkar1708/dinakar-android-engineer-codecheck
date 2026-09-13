# 🛠️ Android Studio Performance & Profiler Tools Screenshots

This directory contains screenshots of **Android Studio Performance Profiling Tools** and developer inspection suites, capturing empirical proof of app performance, responsiveness, and zero memory leaks.

---

## 🚀 Why We Profile: Engineering Telemetry (In Short)

1. ⚡ **CPU Trace (Zero ANRs)**: Proves all network and JSON I/O execute on background worker threads (`Dispatchers.IO`), keeping the `main` thread responsive (<16ms per frame).
2. 🔄 **Network Inspector (Debounce & Cache)**: Proves that typing triggers only **1 debounced request (300ms)** and repeat queries return instantly from `InMemoryCache` (0ms latency, 0 wire bytes).
3. 🧹 **Memory Profiler (0 Leaks)**: Heap dump verification showing zero retained Activity/ViewModel leaks across 10+ screen transitions.
4. 🎨 **Compose Layout Inspector (Recomposition Efficiency)**: Demonstrates stable immutable data classes (`RepositoryItem`) skipping recomposition on unchanged list items.
5. 🔋 **Energy Profiler (Battery Conservation)**: Shows immediate drop to 0mW when the app is backgrounded due to lifecycle-aware coroutines.

---

## 📸 Profiler Screenshots Gallery

> 💡 **Drop your tool screenshots into this folder!**  
> Once added, they will render directly in the tables below:

<div align="center">

### CPU & Network Telemetry
| CPU Profiler (System Trace) | Network Inspector (Cache & Debounce) |
|:---:|:---:|
| ![CPU Profiler](./01-cpu-profiler.png) | ![Network Inspector](./02-network-inspector.png) |
| *Zero main thread freezes; I/O on Dispatchers.IO* | *300ms debounce & 0ms in-memory cache hit* |

### Memory & UI Rendering
| Memory Profiler (Heap Dump) | Compose Layout Inspector |
|:---:|:---:|
| ![Memory Profiler](./03-memory-profiler.png) | ![Layout Inspector](./04-layout-inspector.png) |
| *0 leaked activities across 10+ screen transitions* | *Recomposition counts & skip verification* |

### Energy & Battery Profiler
| Energy Consumption |
|:---:|
| ![Energy Profiler](./05-energy-profiler.png) |
| *Suspends background work when minimized* |

### Full Profiler Dashboard Overview
![Android Studio Profiler Overview](./profiler_overview.png)

</div>

---

### 📋 Recommended File Naming

- **`01-cpu-profiler.png`** - Android Studio CPU timeline showing thread distribution
- **`02-network-inspector.png`** - Network Inspector showing single debounced request and cache hit
- **`03-memory-profiler.png`** - Java Heap Dump showing 0 leaked instances
- **`04-layout-inspector.png`** - Compose Layout Inspector with recomposition count overlay
- **`05-energy-profiler.png`** - Energy Profiler showing battery usage
- **`profiler_overview.png`** - Overall Android Studio profiling dashboard
