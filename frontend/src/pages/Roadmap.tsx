import { useState, useEffect, useRef } from "react";
import {
  Box,
  Typography,
  Grid,
  Paper,
  Tabs,
  Tab,
  Card,
  CardContent,
  LinearProgress,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Chip,
  Button,
  Avatar,
  Divider,
  Stepper,
  Step,
  StepLabel,
  StepContent,
} from "@mui/material";
import {
  Timeline,
  Code as CodeIcon,
  Cloud as CloudIcon,
  CheckCircle as CheckIcon,
  Warning as WarningIcon,
  PlayArrow as PlayIcon,
  Refresh as RefreshIcon,
  Storage as StorageIcon,
  Dns as DnsIcon,
  Settings as SettingsIcon,
  Security as SecurityIcon,
  Map as MapIcon,
  Terminal as TerminalIcon,
  Speed as SpeedIcon,
  OfflineBolt as BoltIcon,
} from "@mui/icons-material";
import { motion, AnimatePresence } from "framer-motion";
import {
  ResponsiveContainer,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip as RechartsTooltip,
} from "recharts";

// Mock Data for Agile Epics
interface Feature {
  name: string;
  status: "done" | "in_progress" | "backlog";
}

interface Epic {
  id: number;
  title: string;
  progress: number;
  status: "Completed" | "In Progress" | "Planning" | "Backlog";
  color: string;
  icon: string;
  features: Feature[];
}

const EPICS_DATA: Epic[] = [
  {
    id: 1,
    title: "Epic 1 – Patient Lifecycle Management",
    progress: 95,
    status: "In Progress",
    color: "#1565C0",
    icon: "patient",
    features: [
      { name: "Patient Registration & Digital Health ID", status: "done" },
      { name: "Appointment Scheduling & Queue Management", status: "done" },
      { name: "OPD & IPD Management", status: "done" },
      { name: "Emergency Department Workflow", status: "in_progress" },
    ],
  },
  {
    id: 2,
    title: "Epic 2 – Clinical Management",
    progress: 80,
    status: "In Progress",
    color: "#00897B",
    icon: "clinical",
    features: [
      { name: "Electronic Medical Records (EMR)", status: "done" },
      { name: "Prescription & Medication Tracking", status: "done" },
      { name: "Laboratory & Radiology Integration", status: "done" },
      { name: "Operation Theatre Scheduling & Surgery Notes", status: "in_progress" },
    ],
  },
  {
    id: 3,
    title: "Epic 3 – Financial & Resource Management",
    progress: 50,
    status: "In Progress",
    color: "#EF6C00",
    icon: "finance",
    features: [
      { name: "Billing & Invoicing", status: "done" },
      { name: "Insurance Claims Processing", status: "in_progress" },
      { name: "Pharmacy Management", status: "backlog" },
      { name: "Inventory & Procurement", status: "backlog" },
    ],
  },
  {
    id: 4,
    title: "Epic 4 – Communication & Collaboration",
    progress: 30,
    status: "Planning",
    color: "#7B1FA2",
    icon: "chat",
    features: [
      { name: "Patient Portal (Reports, Prescriptions, Bills)", status: "done" },
      { name: "Internal Staff Communication & Alerts", status: "in_progress" },
      { name: "Mobile App for Doctors & Patients", status: "backlog" },
    ],
  },
  {
    id: 5,
    title: "Epic 5 – Analytics & Compliance",
    progress: 15,
    status: "Backlog",
    color: "#C62828",
    icon: "analytics",
    features: [
      { name: "Hospital Performance Dashboards", status: "in_progress" },
      { name: "Patient Health Trends & Predictive Analytics", status: "backlog" },
      { name: "Regulatory Compliance (HIPAA, NDHM)", status: "done" },
      { name: "AI-Based Risk Alerts (Sepsis, Prioritization)", status: "backlog" },
    ],
  },
];

// Mock Data for Sprints
interface Sprint {
  id: number;
  name: string;
  duration: string;
  status: "Completed" | "Active" | "Future";
  goals: string;
  details: string[];
}

const SPRINTS_DATA: Sprint[] = [
  {
    id: 1,
    name: "Sprint 1: Base Foundations",
    duration: "2 Weeks (Completed)",
    status: "Completed",
    goals: "Cloud infrastructure setup, CI/CD pipeline, authentication & role-based access.",
    details: [
      "Initialize Spring Boot and React projects inside Docker containers.",
      "Configure Spring Security & JWT generation flow.",
      "Build deployment pipeline via GitHub Actions.",
      "Provision RDS MySQL & MinIO/S3 buckets locally.",
    ],
  },
  {
    id: 2,
    name: "Sprint 2: Core Workflows",
    duration: "2 Weeks (Completed)",
    status: "Completed",
    goals: "Patient registration, appointment scheduling, and doctor availability management.",
    details: [
      "Develop patient registration APIs and frontend lookup.",
      "Implement doctor profiles, availability schedules, and OPD/IPD visits.",
      "Create appointment booking engine with real-time status transitions.",
    ],
  },
  {
    id: 3,
    name: "Sprint 3: Clinical Modules",
    duration: "2 Weeks (Completed)",
    status: "Completed",
    goals: "EMR (Electronic Medical Records) module with clinical notes, diagnoses, and prescriptions.",
    details: [
      "Implement EMR data models and Secure REST endpoints.",
      "Build doctor's clinical workspace UI to write diagnostics.",
      "Create prescription modules with automatic medicine inventory checks.",
    ],
  },
  {
    id: 4,
    name: "Sprint 4: Diagnostics Integration",
    duration: "2 Weeks (Completed)",
    status: "Completed",
    goals: "Laboratory & radiology integration, report storage, and patient file portal.",
    details: [
      "Integrate AWS S3 upload utility in Spring Boot.",
      "Build Lab report portal supporting PDF uploads.",
      "Create FHIR-compliant diagnostic endpoints for interoperability.",
    ],
  },
  {
    id: 5,
    name: "Sprint 5: Billing & Claims",
    duration: "2 Weeks (In Progress)",
    status: "Active",
    goals: "Billing, invoicing engine, payment integrations, and insurance claims processing.",
    details: [
      "Implement billing API calculating consultation fees + pharmacy charges.",
      "Add insurance claim tables, approval status tracking, and provider mappings.",
      "Build checkout interface supporting CASH, CARD, and UPI payments.",
    ],
  },
  {
    id: 6,
    name: "Sprint 6: Inventory & Supply",
    duration: "2 Weeks (Future)",
    status: "Future",
    goals: "Pharmacy stock management, reorder level alerts, and procurement tracking.",
    details: [
      "Develop medicine catalog with automated stock reduction on dispensing.",
      "Create supplier management modules.",
      "Implement automated reorder email trigger system.",
    ],
  },
  {
    id: 7,
    name: "Sprint 7: Intelligence & Analytics",
    duration: "2 Weeks (Future)",
    status: "Future",
    goals: "Performance dashboards, HIPAA compliance audits, and AI-enabled risk warnings.",
    details: [
      "Build Hospital Performance Dashboard with Recharts.",
      "Implement background AI scheduling job to detect patient sepsis risks.",
      "Finalize security audits and exportable HIPAA activity logs.",
    ],
  },
];

// DevOps Pipeline Simulator steps
interface PipelineNode {
  id: string;
  label: string;
  tool: string;
  status: "idle" | "running" | "success" | "failed";
  details: string;
  config: string;
}

// Cloud Component interface
interface CloudComponent {
  id: string;
  name: string;
  type: string;
  status: "Healthy" | "Degraded" | "Idle";
  region: string;
  desc: string;
  metricLabel: string;
  metricMax: number;
}

const CLOUD_COMPONENTS: CloudComponent[] = [
  {
    id: "cdn",
    name: "Amazon CloudFront CDN",
    type: "CDN & Edge Routing",
    status: "Healthy",
    region: "Edge Locations",
    desc: "Caches and serves the compiled React frontend, CSS, and media assets globally with SSL termination.",
    metricLabel: "Active Edge Connections",
    metricMax: 500,
  },
  {
    id: "alb",
    name: "Application Load Balancer",
    type: "Traffic Distributor",
    status: "Healthy",
    region: "ap-south-1",
    desc: "Ingresses web requests and routes them to active nodes inside the Kubernetes cluster.",
    metricLabel: "Requests per Second (RPS)",
    metricMax: 80,
  },
  {
    id: "eks",
    name: "AWS EKS (Kubernetes)",
    type: "Orchestration Cluster",
    status: "Healthy",
    region: "ap-south-1",
    desc: "Orchestrates containerized microservices. Hosts 3 replica pods of the Spring Boot Backend and Nginx Frontend.",
    metricLabel: "Cluster CPU Core Utilization (%)",
    metricMax: 100,
  },
  {
    id: "rds",
    name: "Amazon RDS (MySQL 8.4)",
    type: "Relational Database",
    status: "Healthy",
    region: "ap-south-1 (Multi-AZ)",
    desc: "Primary transactional database for Patient records, Billing details, and Security users. Replicated for high availability.",
    metricLabel: "Database Connections",
    metricMax: 150,
  },
  {
    id: "s3",
    name: "Amazon S3 Bucket",
    type: "Object Storage",
    status: "Healthy",
    region: "ap-south-1",
    desc: "Secure, encrypted object storage for patient diagnostic files, radiology scans, and laboratory report PDFs.",
    metricLabel: "Active File Operations / Min",
    metricMax: 120,
  },
  {
    id: "apigw",
    name: "API Gateway",
    type: "Integrations Router",
    status: "Idle",
    region: "ap-south-1",
    desc: "Routes secure outbound calls to external Insurance systems, diagnostic labs, and HL7 FHIR compliance systems.",
    metricLabel: "API Request Load (%)",
    metricMax: 100,
  },
];

export default function Roadmap() {
  const [tabValue, setTabValue] = useState(0);

  // Agile tab state
  const [activeEpic, setActiveEpic] = useState<Epic | null>(EPICS_DATA[0]);

  // Scrum tab state
  const [selectedSprint, setSelectedSprint] = useState<Sprint>(SPRINTS_DATA[4]); // default to Active Sprint 5

  // DevOps Simulator state
  const [pipelineRunStatus, setPipelineRunStatus] = useState<"idle" | "running" | "success" | "failed">("idle");
  const [pipelineSteps, setPipelineSteps] = useState<PipelineNode[]>([
    { id: "github", label: "Source", tool: "GitHub / GitLab", status: "idle", details: "Pushes repository branch triggering webhooks", config: "Branch: main, triggers: [push, pr]" },
    { id: "actions", label: "CI Engine", tool: "GitHub Actions", status: "idle", details: "Runs build scripts and automated test suites", config: "Runner: ubuntu-latest, JDK: 17, Node: 18" },
    { id: "sonar", label: "Security", tool: "SonarQube & HIPAA", status: "idle", details: "Scans code for secrets, vulnerabilities, and HIPAA compliance", config: "Quality Gate: Strict, Code Coverage > 80%" },
    { id: "nexus", label: "Artifacts", tool: "Nexus Repository", status: "idle", details: "Publishes built JAR and NPM artifacts securely", config: "Repository type: Private Maven/NPM Registry" },
    { id: "docker", label: "Containers", tool: "Docker Build", status: "idle", details: "Packages backend & frontend as lightweight Docker images", config: "Target: AWS ECR / Local Docker Registry" },
    { id: "k8s", label: "Orchestration", tool: "Kubernetes Deploy", status: "idle", details: "Performs zero-downtime rolling update on EKS cluster", config: "Strategy: RollingUpdate, Replicas: 3" },
    { id: "grafana", label: "Monitoring", tool: "Prometheus & Grafana", status: "idle", details: "Checks metrics, CPU thresholds, and error rates post-deploy", config: "Alerting: Slack channels on Pod degradation" },
  ]);
  const [activeNode, setActiveNode] = useState<PipelineNode | null>(null);
  const [simulatedLogs, setSimulatedLogs] = useState<string[]>([]);
  const logTerminalRef = useRef<HTMLDivElement>(null);

  // Cloud tab state
  const [selectedCloudNode, setSelectedCloudNode] = useState<CloudComponent>(CLOUD_COMPONENTS[2]); // default EKS
  const [cloudMetricData, setCloudMetricData] = useState<{ time: string; value: number }[]>([]);

  // Simulate Cloud Metrics ticking
  useEffect(() => {
    const generateInitialData = () => {
      const data = [];
      const now = new Date();
      for (let i = 9; i >= 0; i--) {
        const t = new Date(now.getTime() - i * 5000);
        data.push({
          time: t.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", second: "2-digit" }),
          value: Math.floor(Math.random() * (selectedCloudNode.metricMax * 0.4)) + Math.floor(selectedCloudNode.metricMax * 0.3),
        });
      }
      return data;
    };

    setCloudMetricData(generateInitialData());

    const interval = setInterval(() => {
      setCloudMetricData((prevData) => {
        const nextData = [...prevData.slice(1)];
        const now = new Date();
        nextData.push({
          time: now.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", second: "2-digit" }),
          value: Math.max(
            5,
            Math.min(
              selectedCloudNode.metricMax,
              Math.floor(
                (prevData[prevData.length - 1]?.value || selectedCloudNode.metricMax * 0.5) +
                  (Math.random() * 20 - 10)
              )
            )
          ),
        });
        return nextData;
      });
    }, 4000);

    return () => clearInterval(interval);
  }, [selectedCloudNode]);

  // Simulate CI/CD pipeline run
  const triggerDevOpsPipeline = () => {
    if (pipelineRunStatus === "running") return;

    setPipelineRunStatus("running");
    setSimulatedLogs([]);
    setActiveNode(null);

    // Reset steps to running or idle
    setPipelineSteps((steps) => steps.map((s) => ({ ...s, status: "idle" })));

    const logStatements = [
      // GitHub
      { stepIdx: 0, log: "🚀 Webhook received: Commit #a4f21d pushed by DevOps-Lead", delay: 100 },
      { stepIdx: 0, log: "📦 Fetching branch 'main' from GitHub...", delay: 600 },
      { stepIdx: 0, log: "✅ Source Code checked out successfully. Revision 1.0.4", delay: 1100 },
      // Actions
      { stepIdx: 1, log: "🔨 Initializing GitHub Actions runner: ubuntu-latest...", delay: 1600 },
      { stepIdx: 1, log: "⚙️ Installing JDK 17 & Node.js 18 modules...", delay: 2200 },
      { stepIdx: 1, log: "🚀 Gradle build started: ./gradlew clean compileJava compileTestJava", delay: 2800 },
      { stepIdx: 1, log: "🧪 Running 117 JUnit Unit & Integration Tests...", delay: 3500 },
      { stepIdx: 1, log: "✅ [SUCCESS] 117/117 backend tests passed. (Service layer coverage: 100%)", delay: 4200 },
      { stepIdx: 1, log: "🧪 Running React Frontend test suite (Vite + Vitest)...", delay: 4800 },
      { stepIdx: 1, log: "✅ [SUCCESS] 42 frontend tests passed.", delay: 5300 },
      // SonarQube
      { stepIdx: 2, log: "🔍 Triggering SonarQube Static Code Analysis...", delay: 5900 },
      { stepIdx: 2, log: "🔒 Initiating automated HIPAA compliance & security verification scan...", delay: 6500 },
      { stepIdx: 2, log: "📊 [ANALYSIS] Code Duplication: 0.8%, Code Smells: 2, Security Vulnerabilities: 0", delay: 7200 },
      { stepIdx: 2, log: "✅ [HIPAA] Data encryption configuration verified. Access control filters active.", delay: 7800 },
      // Nexus
      { stepIdx: 3, log: "📦 Archiving build artifacts: backend-1.0.4.jar, frontend-build.tar.gz", delay: 8400 },
      { stepIdx: 3, log: "📤 Uploading artifacts to Nexus Private Repository...", delay: 9000 },
      { stepIdx: 3, log: "✅ Artifacts saved in Nexus repository: multicare/hms-core/1.0.4", delay: 9600 },
      // Docker
      { stepIdx: 4, log: "🐳 Initializing Docker Build for 'multicare-backend' & 'multicare-frontend'...", delay: 10200 },
      { stepIdx: 4, log: "🐳 Building Multi-Stage Dockerfile (Alpine runtime)...", delay: 10800 },
      { stepIdx: 4, log: "🐳 Pushing Docker Images to AWS Elastic Container Registry (ECR)...", delay: 11400 },
      { stepIdx: 4, log: "✅ [DOCKER] backend:latest and frontend:latest tags pushed successfully.", delay: 12000 },
      // K8s
      { stepIdx: 5, log: "☸️ Accessing Kubernetes cluster: AWS EKS (ap-south-1)...", delay: 12600 },
      { stepIdx: 5, log: "☸️ Applying deployment configurations: backend-deployment.yaml, frontend-deployment.yaml...", delay: 13200 },
      { stepIdx: 5, log: "☸️ Triggering rolling update. Replacing old pods: 3 replicas per service...", delay: 13800 },
      { stepIdx: 5, log: "☸️ Healthcheck check: backend-pod-8f4b-1 (Healthy), backend-pod-8f4b-2 (Healthy)", delay: 14500 },
      { stepIdx: 5, log: "✅ [K8s] Rolling update completed with 0 downtime.", delay: 15100 },
      // Grafana
      { stepIdx: 6, log: "📈 Connecting to Prometheus data nodes. Fetching container resources...", delay: 15700 },
      { stepIdx: 6, log: "📊 Dashboard telemetry updated. Heap memory: 412MB, CPU limit: 12.4%", delay: 16300 },
      { stepIdx: 6, log: "🎉 [SUCCESS] Deployment pipeline completed successfully. Build #1.0.4 is LIVE!", delay: 17000 },
    ];

    logStatements.forEach((item) => {
      setTimeout(() => {
        setSimulatedLogs((prev) => [...prev, item.log]);

        // Update active step status
        setPipelineSteps((steps) => {
          const newSteps = [...steps];
          const currentStep = newSteps[item.stepIdx];

          // Set previous step to success if it was running
          if (item.stepIdx > 0 && newSteps[item.stepIdx - 1].status !== "success") {
            newSteps[item.stepIdx - 1].status = "success";
          }

          currentStep.status = "running";
          return newSteps;
        });

        // Auto-scroll terminal
        if (logTerminalRef.current) {
          logTerminalRef.current.scrollTop = logTerminalRef.current.scrollHeight;
        }
      }, item.delay);
    });

    // Complete the final step
    setTimeout(() => {
      setPipelineSteps((steps) => {
        const newSteps = [...steps];
        newSteps[newSteps.length - 1].status = "success";
        return newSteps;
      });
      setPipelineRunStatus("success");
    }, 17500);
  };

  return (
    <Box sx={{ p: 3 }}>
      {/* Page Header */}
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 4, flexWrap: "wrap", gap: 2 }}>
        <Box>
          <Typography variant="h4" sx={{ fontWeight: 800, color: "primary.dark", display: "flex", alignItems: "center", gap: 1.5 }}>
            <MapIcon fontSize="large" color="primary" />
            HMS Implementation Roadmap
          </Typography>
          <Typography color="text.secondary" sx={{ mt: 0.5 }}>
            Visualize and simulate the Agile development lifecycle, DevOps automated CI/CD pipeline, and Cloud architecture.
          </Typography>
        </Box>
        <Chip
          icon={<BoltIcon sx={{ color: "#fff !important" }} />}
          label="Agile + DevOps + Cloud Native"
          sx={{
            bgcolor: "primary.main",
            color: "#fff",
            fontWeight: 700,
            fontSize: 14,
            px: 1.5,
            py: 2.2,
            borderRadius: 3,
            boxShadow: "0 4px 14px rgba(21, 101, 192, 0.3)",
          }}
        />
      </Box>

      {/* Tabs Menu */}
      <Paper sx={{ mb: 4, borderRadius: 4, overflow: "hidden", boxShadow: "0 4px 20px rgba(0,0,0,0.04)" }}>
        <Tabs
          value={tabValue}
          onChange={(_, val) => setTabValue(val)}
          variant="fullWidth"
          textColor="primary"
          indicatorColor="primary"
          sx={{
            bgcolor: "#fff",
            "& .MuiTab-root": {
              fontWeight: 600,
              fontSize: 15,
              py: 2,
              display: "flex",
              flexDirection: "row",
              gap: 1,
            },
          }}
        >
          <Tab icon={<Timeline />} label="Agile Epics Backlog" />
          <Tab icon={<SettingsIcon />} label="Scrum Sprints" />
          <Tab icon={<CodeIcon />} label="CI/CD Pipeline Run" />
          <Tab icon={<CloudIcon />} label="Cloud Infrastructure" />
        </Tabs>
      </Paper>

      {/* Tab Contents */}
      <AnimatePresence mode="wait">
        <motion.div
          key={tabValue}
          initial={{ opacity: 0, y: 15 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -15 }}
          transition={{ duration: 0.25 }}
        >
          {/* TAB 0: AGILE EPICS BACKLOG */}
          {tabValue === 0 && (
            <Grid container spacing={3}>
              {/* Left Column: Epic list */}
              <Grid size={{ xs: 12, md: 5 }}>
                <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                  <Typography variant="h6" sx={{ fontWeight: 700, px: 1, color: "text.primary" }}>
                    High-Level Product Backlog
                  </Typography>

                  {EPICS_DATA.map((epic) => (
                    <Card
                      key={epic.id}
                      onClick={() => setActiveEpic(epic)}
                      sx={{
                        cursor: "pointer",
                        borderRadius: 4,
                        border: "2px solid",
                        borderColor: activeEpic?.id === epic.id ? epic.color : "transparent",
                        bgcolor: activeEpic?.id === epic.id ? `${epic.color}05` : "background.paper",
                        boxShadow: "0 4px 16px rgba(0,0,0,0.04)",
                        transition: "0.2s",
                        "&:hover": {
                          transform: "translateY(-2px)",
                          boxShadow: "0 6px 20px rgba(0,0,0,0.08)",
                        },
                      }}
                    >
                      <CardContent sx={{ p: 2.5 }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 1.5 }}>
                          <Typography sx={{ fontWeight: 700, fontSize: 15, color: epic.color }}>
                            {epic.title.split(" – ")[0]}
                          </Typography>
                          <Chip
                            label={epic.status}
                            size="small"
                            sx={{
                              bgcolor:
                                epic.status === "Completed"
                                  ? "success.main"
                                  : epic.status === "In Progress"
                                  ? "primary.main"
                                  : epic.status === "Planning"
                                  ? "warning.main"
                                  : "text.secondary",
                              color: "#fff",
                              fontWeight: 700,
                              fontSize: 10,
                            }}
                          />
                        </Box>

                        <Typography variant="h6" sx={{ fontWeight: 700, fontSize: 16, mb: 2, color: "text.primary" }}>
                          {epic.title.split(" – ")[1]}
                        </Typography>

                        <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                          <Box sx={{ flexGrow: 1 }}>
                            <LinearProgress
                              variant="determinate"
                              value={epic.progress}
                              sx={{
                                height: 8,
                                borderRadius: 4,
                                bgcolor: `${epic.color}15`,
                                "& .MuiLinearProgress-bar": {
                                  bgcolor: epic.color,
                                  borderRadius: 4,
                                },
                              }}
                            />
                          </Box>
                          <Typography sx={{ fontWeight: 700, fontSize: 14, color: "text.secondary", minWidth: 32 }}>
                            {epic.progress}%
                          </Typography>
                        </Box>
                      </CardContent>
                    </Card>
                  ))}
                </Box>
              </Grid>

              {/* Right Column: Epic Detail features */}
              <Grid size={{ xs: 12, md: 7 }}>
                {activeEpic ? (
                  <Card sx={{ borderRadius: 4, height: "100%", boxShadow: "0 4px 20px rgba(0,0,0,0.06)", border: "1px solid", borderColor: "divider" }}>
                    <CardContent sx={{ p: 4 }}>
                      <Box sx={{ display: "flex", alignItems: "center", gap: 2, mb: 3 }}>
                        <Avatar sx={{ bgcolor: `${activeEpic.color}15`, color: activeEpic.color, width: 56, height: 56 }}>
                          <Timeline />
                        </Avatar>
                        <Box>
                          <Typography variant="h5" sx={{ fontWeight: 800, color: "text.primary" }}>
                            {activeEpic.title}
                          </Typography>
                          <Typography color="text.secondary" variant="body2">
                            Agile Development Tracking • Sprint Backlog Items
                          </Typography>
                        </Box>
                      </Box>

                      <Divider sx={{ mb: 3 }} />

                      <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
                        Epic Features Checklist
                      </Typography>

                      <List sx={{ display: "flex", flexDirection: "column", gap: 1.5 }}>
                        {activeEpic.features.map((feature, idx) => (
                          <ListItem
                            key={idx}
                            sx={{
                              p: 2,
                              borderRadius: 3,
                              bgcolor: feature.status === "done" ? "success.50" : feature.status === "in_progress" ? "primary.50" : "action.hover",
                              border: "1px solid",
                              borderColor:
                                feature.status === "done"
                                  ? "success.light"
                                  : feature.status === "in_progress"
                                  ? "primary.light"
                                  : "divider",
                            }}
                          >
                            <ListItemIcon>
                              {feature.status === "done" ? (
                                <CheckIcon color="success" />
                              ) : feature.status === "in_progress" ? (
                                <WarningIcon color="primary" />
                              ) : (
                                <SettingsIcon color="action" />
                              )}
                            </ListItemIcon>
                            <ListItemText
                              primary={feature.name}
                              slotProps={{
                                primary: {
                                  sx: {
                                    fontWeight: 600,
                                    color: feature.status === "done" ? "success.main" : feature.status === "in_progress" ? "primary.dark" : "text.secondary",
                                  },
                                },
                              }}
                            />
                            <Chip
                              label={feature.status === "done" ? "Completed" : feature.status === "in_progress" ? "In Progress" : "Backlog"}
                              size="small"
                              sx={{
                                fontWeight: 700,
                                fontSize: 11,
                                bgcolor:
                                  feature.status === "done"
                                    ? "success.main"
                                    : feature.status === "in_progress"
                                    ? "primary.main"
                                    : "divider",
                                color: "#fff",
                              }}
                            />
                          </ListItem>
                        ))}
                      </List>

                      <Box sx={{ mt: 4, p: 3, borderRadius: 3, bgcolor: `${activeEpic.color}05`, border: "1px dashed", borderColor: activeEpic.color }}>
                        <Typography variant="subtitle1" sx={{ fontWeight: 700, color: activeEpic.color, mb: 1, display: "flex", alignItems: "center", gap: 1 }}>
                          <BoltIcon /> Agile Execution Strategy
                        </Typography>
                        <Typography variant="body2" color="text.secondary" sx={{ lineHeight: 1.6 }}>
                          Each high-level feature undergoes modular testing, security scanning, containerization, and is deployed incrementally using our DevOps CI/CD pipeline on AWS EKS to achieve zero-downtime rolling updates.
                        </Typography>
                      </Box>
                    </CardContent>
                  </Card>
                ) : (
                  <Paper sx={{ p: 4, textAlign: "center", color: "text.secondary", borderRadius: 4 }}>
                    Select an Epic to inspect features status.
                  </Paper>
                )}
              </Grid>
            </Grid>
          )}

          {/* TAB 1: SCRUM SPRINTS */}
          {tabValue === 1 && (
            <Grid container spacing={3}>
              {/* Left Column: Sprints stepper timeline */}
              <Grid size={{ xs: 12, md: 5 }}>
                <Paper sx={{ p: 3, borderRadius: 4, boxShadow: "0 4px 20px rgba(0,0,0,0.04)" }}>
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 3 }}>
                    Scrum Roadmap (2-Week Sprints)
                  </Typography>

                  <Stepper orientation="vertical" activeStep={SPRINTS_DATA.indexOf(selectedSprint)}>
                    {SPRINTS_DATA.map((sprint) => (
                      <Step key={sprint.id}>
                        <StepLabel
                          onClick={() => setSelectedSprint(sprint)}
                          sx={{
                            cursor: "pointer",
                            "& .MuiStepLabel-label": {
                              fontWeight: selectedSprint.id === sprint.id ? 700 : 500,
                              fontSize: 15,
                              color: selectedSprint.id === sprint.id ? "primary.main" : "text.secondary",
                            },
                          }}
                        >
                          <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                            <Typography sx={{ fontWeight: 700 }}>{sprint.name.split(":")[0]}</Typography>
                            <Chip
                              label={sprint.status}
                              size="small"
                              variant="outlined"
                              color={sprint.status === "Completed" ? "success" : sprint.status === "Active" ? "primary" : "default"}
                              sx={{ height: 18, fontSize: 9, fontWeight: 700 }}
                            />
                          </Box>
                          <Typography variant="caption" color="text.secondary">
                            {sprint.duration}
                          </Typography>
                        </StepLabel>
                        <StepContent>
                          <Typography variant="body2" color="text.secondary" sx={{ mt: 1, mb: 1 }}>
                            {sprint.goals}
                          </Typography>
                        </StepContent>
                      </Step>
                    ))}
                  </Stepper>
                </Paper>
              </Grid>

              {/* Right Column: Sprint details & deploy trigger */}
              <Grid size={{ xs: 12, md: 7 }}>
                <Card sx={{ borderRadius: 4, height: "100%", boxShadow: "0 4px 20px rgba(0,0,0,0.06)", border: "1px solid", borderColor: "divider" }}>
                  <CardContent sx={{ p: 4 }}>
                    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", mb: 3, flexWrap: "wrap", gap: 2 }}>
                      <Box>
                        <Typography variant="h5" sx={{ fontWeight: 850, color: "primary.dark" }}>
                          {selectedSprint.name}
                        </Typography>
                        <Typography color="text.secondary" variant="body2" sx={{ mt: 0.5 }}>
                          Sprint Cycle Length: 2 Weeks
                        </Typography>
                      </Box>
                      <Chip
                        label={selectedSprint.status}
                        color={selectedSprint.status === "Completed" ? "success" : selectedSprint.status === "Active" ? "primary" : "default"}
                        sx={{ fontWeight: 800, fontSize: 13, px: 1 }}
                      />
                    </Box>

                    <Paper variant="outlined" sx={{ p: 2.5, borderRadius: 3, bgcolor: "primary.50", border: "1px solid", borderColor: "primary.light", mb: 4 }}>
                      <Typography variant="subtitle2" sx={{ fontWeight: 700, color: "primary.dark", mb: 0.5 }}>
                        Sprint Objective
                      </Typography>
                      <Typography variant="body2" sx={{ fontWeight: 500, color: "text.primary" }}>
                        {selectedSprint.goals}
                      </Typography>
                    </Paper>

                    <Typography variant="h6" sx={{ fontWeight: 700, mb: 2 }}>
                      Deliverables Checklist
                    </Typography>

                    <List sx={{ display: "flex", flexDirection: "column", gap: 1.5 }}>
                      {selectedSprint.details.map((detail, idx) => (
                        <ListItem
                          key={idx}
                          sx={{
                            p: 2,
                            borderRadius: 3,
                            bgcolor: "background.paper",
                            border: "1px solid",
                            borderColor: "divider",
                          }}
                        >
                          <ListItemIcon>
                            {selectedSprint.status === "Completed" ? (
                              <CheckIcon color="success" />
                            ) : selectedSprint.status === "Active" ? (
                              <WarningIcon color="primary" />
                            ) : (
                              <SettingsIcon color="action" />
                            )}
                          </ListItemIcon>
                          <ListItemText
                            primary={detail}
                            slotProps={{
                              primary: {
                                sx: {
                                  fontWeight: 500,
                                  fontSize: 14.5,
                                  color: "text.primary",
                                },
                              },
                            }}
                          />
                        </ListItem>
                      ))}
                    </List>

                    {/* Interactive Deployment simulation for this sprint */}
                    <Box sx={{ mt: 5, p: 3, borderRadius: 4, bgcolor: "grey.50", border: "1px solid", borderColor: "divider" }}>
                      <Typography variant="subtitle1" sx={{ fontWeight: 700, mb: 1, display: "flex", alignItems: "center", gap: 1 }}>
                        <TerminalIcon /> Sprint DevOps Artifact Deployment
                      </Typography>
                      <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                        Run a simulated pipeline to package, verify, scan, and deploy this sprint's specific features into the staging/production cloud environment.
                      </Typography>

                      <Button
                        variant="contained"
                        onClick={() => {
                          setTabValue(2);
                          triggerDevOpsPipeline();
                        }}
                        disabled={selectedSprint.status === "Future"}
                        startIcon={<PlayIcon />}
                        sx={{
                          borderRadius: 3,
                          px: 3,
                          py: 1.5,
                          boxShadow: "0 4px 12px rgba(25, 118, 210, 0.2)",
                        }}
                      >
                        Launch Sprint Deployment
                      </Button>
                      {selectedSprint.status === "Future" && (
                        <Typography variant="caption" color="error.main" sx={{ display: "block", mt: 1, fontWeight: 600 }}>
                          * Deployment simulation is locked for future planning sprints.
                        </Typography>
                      )}
                    </Box>
                  </CardContent>
                </Card>
              </Grid>
            </Grid>
          )}

          {/* TAB 2: CI/CD DEVOPS PIPELINE */}
          {tabValue === 2 && (
            <Grid container spacing={3}>
              {/* Top Row: Flowchart diagram */}
              <Grid size={{ xs: 12 }}>
                <Paper sx={{ p: 4, borderRadius: 4, boxShadow: "0 4px 20px rgba(0,0,0,0.04)", overflowX: "auto" }}>
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 4 }}>
                    Interactive DevOps Deployment Pipeline
                  </Typography>

                  <Box sx={{ display: "flex", alignItems: "center", justifyContent: "space-between", minWidth: 900, px: 2, position: "relative" }}>
                    {pipelineSteps.map((step, idx) => (
                      <Box key={step.id} sx={{ display: "flex", alignItems: "center", flexGrow: idx < pipelineSteps.length - 1 ? 1 : 0 }}>
                        {/* Node Card */}
                        <motion.div
                          whileHover={{ scale: 1.05 }}
                          onClick={() => setActiveNode(step)}
                          style={{ cursor: "pointer" }}
                        >
                          <Paper
                            sx={{
                              p: 2,
                              borderRadius: 4,
                              width: 140,
                              height: 120,
                              textAlign: "center",
                              display: "flex",
                              flexDirection: "column",
                              justifyContent: "center",
                              alignItems: "center",
                              gap: 1,
                              border: "2px solid",
                              borderColor:
                                activeNode?.id === step.id
                                  ? "primary.main"
                                  : step.status === "success"
                                  ? "success.main"
                                  : step.status === "running"
                                  ? "warning.main"
                                  : "divider",
                              bgcolor:
                                step.status === "success"
                                  ? "success.50"
                                  : step.status === "running"
                                  ? "warning.50"
                                  : "background.paper",
                              boxShadow:
                                step.status === "running"
                                  ? "0 0 12px rgba(237, 108, 2, 0.4)"
                                  : "0 4px 12px rgba(0,0,0,0.03)",
                            }}
                          >
                            <Box sx={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
                              {step.id === "github" && <CodeIcon color={step.status === "success" ? "success" : "primary"} />}
                              {step.id === "actions" && <BoltIcon color={step.status === "success" ? "success" : "primary"} />}
                              {step.id === "sonar" && <SecurityIcon color={step.status === "success" ? "success" : "primary"} />}
                              {step.id === "nexus" && <StorageIcon color={step.status === "success" ? "success" : "primary"} />}
                              {step.id === "docker" && <DnsIcon color={step.status === "success" ? "success" : "primary"} />}
                              {step.id === "k8s" && <CloudIcon color={step.status === "success" ? "success" : "primary"} />}
                              {step.id === "grafana" && <SpeedIcon color={step.status === "success" ? "success" : "primary"} />}
                            </Box>

                            <Typography sx={{ fontWeight: 700, fontSize: 13, color: "text.primary" }}>
                              {step.label}
                            </Typography>
                            <Typography variant="caption" color="text.secondary" sx={{ fontSize: 11, fontWeight: 600 }}>
                              {step.tool}
                            </Typography>

                            {step.status === "running" && (
                              <Box sx={{ width: "80%", mt: 1 }}>
                                <LinearProgress color="warning" />
                              </Box>
                            )}
                          </Paper>
                        </motion.div>

                        {/* Connector line */}
                        {idx < pipelineSteps.length - 1 && (
                          <Box
                            sx={{
                              flexGrow: 1,
                              height: 4,
                              bgcolor:
                                step.status === "success"
                                  ? "success.main"
                                  : step.status === "running"
                                  ? "warning.main"
                                  : "divider",
                              mx: 1.5,
                              transition: "0.5s",
                              boxShadow:
                                step.status === "success"
                                  ? "0 0 8px rgba(46, 125, 50, 0.4)"
                                  : "none",
                            }}
                          />
                        )}
                      </Box>
                    ))}
                  </Box>
                </Paper>
              </Grid>

              {/* Bottom Left: Node detail / execution trigger */}
              <Grid size={{ xs: 12, md: 5 }}>
                <Card sx={{ borderRadius: 4, height: "100%", boxShadow: "0 4px 20px rgba(0,0,0,0.06)", border: "1px solid", borderColor: "divider" }}>
                  <CardContent sx={{ p: 4, display: "flex", flexDirection: "column", height: "100%", justifyContent: "space-between" }}>
                    <Box>
                      <Typography variant="h6" sx={{ fontWeight: 700, mb: 3 }}>
                        Pipeline Control Panel
                      </Typography>

                      <Button
                        variant="contained"
                        size="large"
                        fullWidth
                        onClick={triggerDevOpsPipeline}
                        disabled={pipelineRunStatus === "running"}
                        startIcon={pipelineRunStatus === "running" ? <RefreshIcon className="rotate-icon" /> : <PlayIcon />}
                        sx={{
                          py: 2,
                          borderRadius: 3,
                          bgcolor: pipelineRunStatus === "running" ? "warning.main" : "primary.main",
                          boxShadow: "0 6px 20px rgba(0,0,0,0.1)",
                        }}
                      >
                        {pipelineRunStatus === "running"
                          ? "Deploying Sprint Artifacts..."
                          : "Run Automated CI/CD Pipeline"}
                      </Button>

                      {activeNode ? (
                        <Box sx={{ mt: 4 }}>
                          <Typography variant="subtitle1" sx={{ fontWeight: 750, color: "primary.main", mb: 1 }}>
                            Active Step: {activeNode.label}
                          </Typography>
                          <Divider sx={{ my: 1 }} />
                          <Typography variant="body2" sx={{ fontWeight: 600, color: "text.primary" }}>
                            Tool: {activeNode.tool}
                          </Typography>
                          <Typography variant="body2" color="text.secondary" sx={{ mt: 1.5, lineHeight: 1.5 }}>
                            {activeNode.details}
                          </Typography>
                          <Box sx={{ mt: 2, p: 2, borderRadius: 2, bgcolor: "action.hover", border: "1px solid", borderColor: "divider" }}>
                            <Typography variant="caption" sx={{ fontFamily: "monospace", display: "block", color: "text.secondary" }}>
                              Config Parameters:
                            </Typography>
                            <Typography variant="caption" sx={{ fontFamily: "monospace", fontWeight: 700 }}>
                              {activeNode.config}
                            </Typography>
                          </Box>
                        </Box>
                      ) : (
                        <Box sx={{ mt: 4, p: 3, border: "1px dashed", borderColor: "divider", borderRadius: 3, textAlign: "center" }}>
                          <Typography color="text.secondary" variant="body2">
                            Click any node in the flowchart above to inspect its purpose, configuration, and logs.
                          </Typography>
                        </Box>
                      )}
                    </Box>

                    <Box sx={{ mt: 4, p: 2, bgcolor: "success.50", border: "1px solid", borderColor: "success.light", borderRadius: 3 }}>
                      <Typography variant="subtitle2" sx={{ color: "success.main", fontWeight: 700, display: "flex", alignItems: "center", gap: 1 }}>
                        <CheckIcon fontSize="small" /> HIPAA Security Checks Active
                      </Typography>
                      <Typography variant="caption" color="text.secondary" sx={{ display: "block", mt: 0.5 }}>
                        The security step scans configs for SSL/TLS setups, database authentication parameters, and data-at-rest encryption before deployment approval.
                      </Typography>
                    </Box>
                  </CardContent>
                </Card>
              </Grid>

              {/* Bottom Right: Pipeline Live log output */}
              <Grid size={{ xs: 12, md: 7 }}>
                <Card sx={{ borderRadius: 4, boxShadow: "0 4px 20px rgba(0,0,0,0.06)", bgcolor: "#1E1E1E", color: "#D4D4D4" }}>
                  <CardContent sx={{ p: 3 }}>
                    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
                      <Typography sx={{ fontWeight: 700, color: "#fff", display: "flex", alignItems: "center", gap: 1 }}>
                        <TerminalIcon /> Interactive Terminal Logs
                      </Typography>
                      <Chip label="Bash Output" size="small" sx={{ bgcolor: "#333", color: "#fff", fontWeight: 700, fontSize: 10 }} />
                    </Box>

                    <Box
                      ref={logTerminalRef}
                      sx={{
                        height: 380,
                        overflowY: "auto",
                        fontFamily: "monospace",
                        fontSize: 12.5,
                        lineHeight: 1.6,
                        p: 2,
                        bgcolor: "#121212",
                        borderRadius: 3,
                        border: "1px solid #333",
                        "&::-webkit-scrollbar": { width: 8 },
                        "&::-webkit-scrollbar-thumb": { bgcolor: "#444", borderRadius: 4 },
                      }}
                    >
                      {simulatedLogs.length === 0 ? (
                        <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100%", color: "#666" }}>
                          No active deployment. Click 'Run Automated CI/CD Pipeline' above to view build output logs in real-time.
                        </Box>
                      ) : (
                        simulatedLogs.map((log, index) => {
                          let color = "#D4D4D4";
                          if (log.includes("[SUCCESS]") || log.includes("successfully") || log.includes("LIVE!")) {
                            color = "#4CAF50";
                          } else if (log.includes("🚀") || log.includes("🐳") || log.includes("☸️")) {
                            color = "#2196F3";
                          } else if (log.includes("[ANALYSIS]") || log.includes("[HIPAA]")) {
                            color = "#00BCD4";
                          } else if (log.includes("Cleaned") || log.includes("Installing")) {
                            color = "#FFEB3B";
                          }

                          return (
                            <div key={index} style={{ color }}>
                              {log}
                            </div>
                          );
                        })
                      )}
                    </Box>
                  </CardContent>
                </Card>
              </Grid>
            </Grid>
          )}

          {/* TAB 3: CLOUD INFRASTRUCTURE */}
          {tabValue === 3 && (
            <Grid container spacing={3}>
              {/* Left Column: Cloud architecture map */}
              <Grid size={{ xs: 12, md: 5 }}>
                <Paper sx={{ p: 3, borderRadius: 4, boxShadow: "0 4px 20px rgba(0,0,0,0.04)", height: "100%" }}>
                  <Typography variant="h6" sx={{ fontWeight: 700, mb: 3 }}>
                    AWS Cloud Deployment Architecture
                  </Typography>

                  <Box sx={{ display: "flex", flexDirection: "column", gap: 1.5 }}>
                    {CLOUD_COMPONENTS.map((node) => (
                      <Card
                        key={node.id}
                        onClick={() => setSelectedCloudNode(node)}
                        sx={{
                          cursor: "pointer",
                          borderRadius: 3.5,
                          border: "2px solid",
                          borderColor: selectedCloudNode.id === node.id ? "primary.main" : "transparent",
                          bgcolor: selectedCloudNode.id === node.id ? "primary.50" : "background.paper",
                          boxShadow: "0 2px 10px rgba(0,0,0,0.02)",
                          transition: "0.2s",
                          "&:hover": {
                            transform: "translateX(4px)",
                          },
                        }}
                      >
                        <CardContent sx={{ p: 2, display: "flex", alignItems: "center", gap: 2 }}>
                          <Avatar
                            sx={{
                              bgcolor:
                                node.id === "eks"
                                  ? "#FF9900"
                                  : node.id === "rds"
                                  ? "#3b73c4"
                                  : node.id === "s3"
                                  ? "#3f9c35"
                                  : node.id === "cdn"
                                  ? "#cc3399"
                                  : "#666",
                              color: "#fff",
                              width: 44,
                              height: 44,
                            }}
                          >
                            {node.id === "eks" && <CloudIcon />}
                            {node.id === "rds" && <StorageIcon />}
                            {node.id === "s3" && <DnsIcon />}
                            {node.id === "cdn" && <SettingsIcon />}
                            {node.id === "apigw" && <BoltIcon />}
                          </Avatar>

                          <Box sx={{ flexGrow: 1 }}>
                            <Typography sx={{ fontWeight: 700, fontSize: 14.5 }}>
                              {node.name}
                            </Typography>
                            <Typography variant="caption" color="text.secondary">
                              {node.type} • {node.region}
                            </Typography>
                          </Box>

                          <Chip
                            label={node.status}
                            size="small"
                            color={node.status === "Healthy" ? "success" : "default"}
                            sx={{ fontWeight: 700, fontSize: 10, height: 18 }}
                          />
                        </CardContent>
                      </Card>
                    ))}
                  </Box>
                </Paper>
              </Grid>

              {/* Right Column: Node details & Live Telemetry Charts */}
              <Grid size={{ xs: 12, md: 7 }}>
                <Card sx={{ borderRadius: 4, boxShadow: "0 4px 20px rgba(0,0,0,0.06)", border: "1px solid", borderColor: "divider", height: "100%" }}>
                  <CardContent sx={{ p: 4, display: "flex", flexDirection: "column", height: "100%", justifyContent: "space-between" }}>
                    <Box>
                      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", mb: 3 }}>
                        <Box>
                          <Typography variant="h5" sx={{ fontWeight: 800, color: "primary.dark" }}>
                            {selectedCloudNode.name}
                          </Typography>
                          <Typography color="text.secondary" variant="body2">
                            {selectedCloudNode.type}
                          </Typography>
                        </Box>
                        <Chip
                          label={`Region: ${selectedCloudNode.region}`}
                          variant="outlined"
                          color="primary"
                          sx={{ fontWeight: 700 }}
                        />
                      </Box>

                      <Typography variant="body1" sx={{ color: "text.primary", mb: 4, lineHeight: 1.6 }}>
                        {selectedCloudNode.desc}
                      </Typography>

                      <Divider sx={{ mb: 4 }} />

                      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
                        <Typography variant="h6" sx={{ fontWeight: 700 }}>
                          Live Cloud Metric Telemetry
                        </Typography>
                        <Chip label="Real-time simulated" color="success" size="small" variant="outlined" sx={{ fontWeight: 600 }} />
                      </Box>

                      {/* Line chart of simulated metrics */}
                      <ResponsiveContainer width="100%" height={240}>
                        <AreaChart data={cloudMetricData}>
                          <defs>
                            <linearGradient id="colorMetric" x1="0" y1="0" x2="0" y2="1">
                              <stop offset="5%" stopColor="#1565C0" stopOpacity={0.4} />
                              <stop offset="95%" stopColor="#1565C0" stopOpacity={0.0} />
                            </linearGradient>
                          </defs>
                          <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
                          <XAxis dataKey="time" tick={{ fontSize: 10 }} />
                          <YAxis domain={[0, selectedCloudNode.metricMax]} tick={{ fontSize: 10 }} />
                          <RechartsTooltip />
                          <Area
                            type="monotone"
                            dataKey="value"
                            name={selectedCloudNode.metricLabel}
                            stroke="#1565C0"
                            strokeWidth={3}
                            fillOpacity={1}
                            fill="url(#colorMetric)"
                          />
                        </AreaChart>
                      </ResponsiveContainer>
                    </Box>

                    <Box sx={{ mt: 3, p: 2, borderRadius: 3, bgcolor: "primary.50", border: "1px solid", borderColor: "primary.light" }}>
                      <Typography variant="subtitle2" sx={{ fontWeight: 700, color: "primary.dark" }}>
                        Compliance Status
                      </Typography>
                      <Typography variant="body2" color="text.secondary" sx={{ fontSize: 12.5, mt: 0.5 }}>
                        This cloud component complies with standard HIPAA guidelines. Access logging, TLS 1.3 data transfer protocols, and automated KMS keys for database encryption are fully integrated.
                      </Typography>
                    </Box>
                  </CardContent>
                </Card>
              </Grid>
            </Grid>
          )}
        </motion.div>
      </AnimatePresence>
    </Box>
  );
}
