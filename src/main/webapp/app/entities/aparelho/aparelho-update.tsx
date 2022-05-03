import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAparelho } from 'app/shared/model/aparelho.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './aparelho.reducer';

export const AparelhoUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const aparelhoEntity = useAppSelector(state => state.aparelho.entity);
  const loading = useAppSelector(state => state.aparelho.loading);
  const updating = useAppSelector(state => state.aparelho.updating);
  const updateSuccess = useAppSelector(state => state.aparelho.updateSuccess);
  const statusValues = Object.keys(Status);
  const handleClose = () => {
    props.history.push('/aparelho');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...aparelhoEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'EM_ESPERA',
          ...aparelhoEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="livreTesteApiApp.aparelho.home.createOrEditLabel" data-cy="AparelhoCreateUpdateHeading">
            Create or edit a Aparelho
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="aparelho-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Nome" id="aparelho-nome" name="nome" data-cy="nome" type="text" />
              <ValidatedField label="Numero Serie" id="aparelho-numeroSerie" name="numeroSerie" data-cy="numeroSerie" type="text" />
              <ValidatedField label="Status" id="aparelho-status" name="status" data-cy="status" type="select">
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {status}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField label="Carga" id="aparelho-carga" name="carga" data-cy="carga" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/aparelho" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AparelhoUpdate;
