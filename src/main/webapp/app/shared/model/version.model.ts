import { Moment } from 'moment';
import { IComponentEntity } from 'app/shared/model//component-entity.model';

export interface IVersion {
  id?: number;
  versionString?: string;
  deployedBy?: string;
  gitCommit?: string;
  gitCommitter?: string;
  majorVersion?: number;
  minorVersion?: number;
  hotfixNumber?: number;
  buildNumber?: number;
  createdTime?: Moment;
  udpatedTime?: Moment;
  comp?: IComponentEntity;
}

export const defaultValue: Readonly<IVersion> = {};
