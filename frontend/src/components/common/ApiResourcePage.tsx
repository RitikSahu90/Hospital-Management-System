import { useEffect, useMemo, useState } from "react";
import {
  Alert,
  Box,
  Button,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  InputAdornment,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  TextField,
  Typography,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";

export type FormField = { key: string; label: string; type?: "text" | "number" | "date" | "time" };
type Row = { id: number };

// Fields whose value should be rendered as a colored status chip
const STATUS_FIELDS = new Set(["status", "paymentStatus", "billingStatus", "appointmentStatus"]);

const STATUS_COLOR_MAP: Record<string, "default" | "primary" | "secondary" | "error" | "info" | "success" | "warning"> = {
  // Appointment
  SCHEDULED: "info",
  CONFIRMED: "primary",
  COMPLETED: "success",
  CANCELLED: "error",
  NO_SHOW: "warning",
  // Billing
  PENDING: "warning",
  PAID: "success",
  PARTIALLY_PAID: "info",
  // Doctor
  ACTIVE: "success",
  INACTIVE: "default",
  ON_LEAVE: "warning",
};

interface Props<T extends Row> {
  title: string;
  fields: FormField[];
  load: () => Promise<T[]>;
  create: (values: Record<string, string | number>) => Promise<T>;
  update?: (id: number, values: Record<string, string | number>) => Promise<T>;
  remove?: (id: number) => Promise<unknown>;
  canWrite: boolean;
  canDelete?: boolean;
}

export default function ApiResourcePage<T extends Row>({ title, fields, load, create, update, remove, canWrite, canDelete = false }: Props<T>) {
  const [rows, setRows] = useState<T[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [open, setOpen] = useState(false);
  const [saving, setSaving] = useState(false);
  const [editing, setEditing] = useState<T | null>(null);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const empty = () => Object.fromEntries(fields.map((field) => [field.key, ""]));
  const [values, setValues] = useState<Record<string, string | number>>(empty);

  const refresh = async () => {
    try { setLoading(true); setError(""); setRows(await load()); }
    catch { setError(`Unable to load ${title.toLowerCase()} from the backend.`); }
    finally { setLoading(false); }
  };
  useEffect(() => { void refresh(); }, []);

  // Filter rows by search keyword
  const filteredRows = useMemo(() => {
    if (!search) return rows;
    const keyword = search.toLowerCase();
    return rows.filter((row) => {
      const record = row as Record<string, unknown>;
      return fields.some((field) => String(record[field.key] ?? "").toLowerCase().includes(keyword));
    });
  }, [rows, search, fields]);

  const pagedRows = useMemo(
    () => filteredRows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage),
    [filteredRows, page, rowsPerPage]
  );

  const showDialog = (row?: T) => {
    const record = row as Record<string, unknown> | undefined;
    setEditing(row ?? null);
    setValues(row ? Object.fromEntries(fields.map((field) => [field.key, String(record?.[field.key] ?? "")])) : empty());
    setOpen(true);
  };

  const save = async () => {
    try {
      setSaving(true); setError("");
      const payload = Object.fromEntries(fields.map((field) => [field.key, field.type === "number" ? Number(values[field.key]) : values[field.key]]));
      const saved = editing && update ? await update(editing.id, payload) : await create(payload);
      setRows((current) => editing ? current.map((row) => row.id === saved.id ? saved : row) : [saved, ...current]);
      setOpen(false);
    } catch { setError(`Unable to save ${title.slice(0, -1).toLowerCase()}. Check the values and your permissions.`); }
    finally { setSaving(false); }
  };

  const removeRow = async (id: number) => {
    try { setError(""); await remove?.(id); setRows((current) => current.filter((row) => row.id !== id)); }
    catch { setError(`Unable to delete ${title.slice(0, -1).toLowerCase()}. Check your permissions.`); }
  };

  const renderCell = (field: FormField, value: unknown) => {
    const strValue = String(value ?? "—");

    // Status chip
    if (STATUS_FIELDS.has(field.key) && value) {
      const color = STATUS_COLOR_MAP[strValue] || "default";
      return <Chip label={strValue} size="small" color={color} variant="outlined" sx={{ fontWeight: 600 }} />;
    }

    // Currency formatting
    if ((field.key === "consultationFee" || field.key === "unitPrice" || field.key === "amount" || field.key === "totalAmount" || field.key === "medicineCharges" || field.key === "otherCharges") && value !== null && value !== "") {
      return `₹${Number(value).toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
    }

    // Date formatting
    if (field.type === "date" && value) {
      try {
        return new Date(strValue).toLocaleDateString("en-IN", { day: "2-digit", month: "short", year: "numeric" });
      } catch { return strValue; }
    }

    return strValue;
  };

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3, flexWrap: "wrap", gap: 2 }}>
        <Typography variant="h4" sx={{ fontWeight: "bold" }}>{title}</Typography>
        <Box sx={{ display: "flex", gap: 2, alignItems: "center", flexWrap: "wrap" }}>
          <TextField
            size="small"
            placeholder="Search…"
            value={search}
            onChange={(e) => { setSearch(e.target.value); setPage(0); }}
            sx={{ width: 220 }}
            slotProps={{
              input: {
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon fontSize="small" color="action" />
                  </InputAdornment>
                ),
              },
            }}
          />
          {canWrite && <Button variant="contained" onClick={() => showDialog()}>Add {title.slice(0, -1)}</Button>}
        </Box>
      </Box>

      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}><CircularProgress /></Box>
      ) : pagedRows.length === 0 ? (
        <Paper sx={{ p: 5, textAlign: "center" }}>
          <Typography color="text.secondary">
            {search ? `No ${title.toLowerCase()} match your search.` : `No ${title.toLowerCase()} found.`}
          </Typography>
        </Paper>
      ) : (
        <Paper sx={{ overflowX: "auto", borderRadius: 3 }}>
          <Table>
            <TableHead>
              <TableRow sx={{ bgcolor: "grey.50" }}>
                {fields.map((field) => (
                  <TableCell key={field.key} sx={{ fontWeight: 700, fontSize: 13, textTransform: "uppercase", letterSpacing: 0.5 }}>
                    {field.label}
                  </TableCell>
                ))}
                {(canWrite && update || canDelete && remove) && <TableCell sx={{ fontWeight: 700 }}>Actions</TableCell>}
              </TableRow>
            </TableHead>
            <TableBody>
              {pagedRows.map((row) => {
                const record = row as Record<string, unknown>;
                return (
                  <TableRow key={row.id} hover sx={{ "&:last-child td": { border: 0 } }}>
                    {fields.map((field) => (
                      <TableCell key={field.key}>{renderCell(field, record[field.key])}</TableCell>
                    ))}
                    {(canWrite && update || canDelete && remove) && (
                      <TableCell>
                        {canWrite && update && <Button size="small" onClick={() => showDialog(row)}>Edit</Button>}
                        {canDelete && remove && <Button size="small" color="error" onClick={() => void removeRow(row.id)}>Delete</Button>}
                      </TableCell>
                    )}
                  </TableRow>
                );
              })}
            </TableBody>
          </Table>
          <TablePagination
            component="div"
            count={filteredRows.length}
            page={page}
            onPageChange={(_, newPage) => setPage(newPage)}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={(e) => { setRowsPerPage(parseInt(e.target.value, 10)); setPage(0); }}
            rowsPerPageOptions={[5, 10, 25]}
          />
        </Paper>
      )}

      <Dialog open={open} onClose={() => !saving && setOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>{editing ? "Edit" : "Add"} {title.slice(0, -1)}</DialogTitle>
        <DialogContent>
          <Box sx={{ display: "grid", gap: 2, pt: 1 }}>
            {fields.map((field) => (
              <TextField
                key={field.key}
                label={field.label}
                type={field.type ?? "text"}
                value={values[field.key]}
                onChange={(event) => setValues((current) => ({ ...current, [field.key]: event.target.value }))}
                slotProps={{ inputLabel: field.type === "date" || field.type === "time" ? { shrink: true } : undefined }}
                fullWidth
              />
            ))}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpen(false)} disabled={saving}>Cancel</Button>
          <Button onClick={() => void save()} disabled={saving} variant="contained">{saving ? "Saving..." : "Save"}</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}