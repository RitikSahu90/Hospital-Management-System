import { useEffect, useState } from "react";
import { Alert, Box, Button, CircularProgress, Dialog, DialogActions, DialogContent, DialogTitle, Paper, Table, TableBody, TableCell, TableHead, TableRow, TextField, Typography } from "@mui/material";

export type FormField = { key: string; label: string; type?: "text" | "number" | "date" | "time" };
type Row = { id: number };

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
  const empty = () => Object.fromEntries(fields.map((field) => [field.key, ""]));
  const [values, setValues] = useState<Record<string, string | number>>(empty);

  const refresh = async () => {
    try { setLoading(true); setError(""); setRows(await load()); }
    catch { setError(`Unable to load ${title.toLowerCase()} from the backend.`); }
    finally { setLoading(false); }
  };
  useEffect(() => { void refresh(); }, []);
  const showDialog = (row?: T) => { const record = row as Record<string, unknown> | undefined; setEditing(row ?? null); setValues(row ? Object.fromEntries(fields.map((field) => [field.key, String(record?.[field.key] ?? "")])) : empty()); setOpen(true); };
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

  return <Box sx={{ p: 3 }}>
    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 3 }}><Typography variant="h4" sx={{ fontWeight: "bold" }}>{title}</Typography>{canWrite && <Button variant="contained" onClick={() => showDialog()}>Add {title.slice(0, -1)}</Button>}</Box>
    {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
    {loading ? <Box sx={{ display: "flex", justifyContent: "center", py: 8 }}><CircularProgress /></Box> : rows.length === 0 ? <Paper sx={{ p: 5, textAlign: "center" }}><Typography color="text.secondary">No {title.toLowerCase()} found.</Typography></Paper> : <Paper sx={{ overflowX: "auto" }}><Table><TableHead><TableRow>{fields.map((field) => <TableCell key={field.key}>{field.label}</TableCell>)}{(canWrite && update || canDelete && remove) && <TableCell>Actions</TableCell>}</TableRow></TableHead><TableBody>{rows.map((row) => { const record = row as Record<string, unknown>; return <TableRow key={row.id}>{fields.map((field) => <TableCell key={field.key}>{String(record[field.key] ?? "—")}</TableCell>)}{(canWrite && update || canDelete && remove) && <TableCell>{canWrite && update && <Button size="small" onClick={() => showDialog(row)}>Edit</Button>}{canDelete && remove && <Button size="small" color="error" onClick={() => void removeRow(row.id)}>Delete</Button>}</TableCell>}</TableRow>; })}</TableBody></Table></Paper>}
    <Dialog open={open} onClose={() => !saving && setOpen(false)} fullWidth maxWidth="sm"><DialogTitle>{editing ? "Edit" : "Add"} {title.slice(0, -1)}</DialogTitle><DialogContent><Box sx={{ display: "grid", gap: 2, pt: 1 }}>{fields.map((field) => <TextField key={field.key} label={field.label} type={field.type ?? "text"} value={values[field.key]} onChange={(event) => setValues((current) => ({ ...current, [field.key]: event.target.value }))} slotProps={{ inputLabel: field.type === "date" || field.type === "time" ? { shrink: true } : undefined }} fullWidth />)}</Box></DialogContent><DialogActions><Button onClick={() => setOpen(false)} disabled={saving}>Cancel</Button><Button onClick={() => void save()} disabled={saving} variant="contained">{saving ? "Saving..." : "Save"}</Button></DialogActions></Dialog>
  </Box>;
}
