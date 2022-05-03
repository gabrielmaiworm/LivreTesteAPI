import { Status } from 'app/shared/model/enumerations/status.model';

export interface IAparelho {
  id?: number;
  nome?: string | null;
  numeroSerie?: string | null;
  status?: Status | null;
  carga?: number | null;
}

export const defaultValue: Readonly<IAparelho> = {};
