import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAparelho } from 'app/shared/model/aparelho.model';
import { getEntities } from './aparelho.reducer';

export const Aparelho = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const aparelhoList = useAppSelector(state => state.aparelho.entities);
  const loading = useAppSelector(state => state.aparelho.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="aparelho-heading" data-cy="AparelhoHeading">
        Aparelhos
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to="/aparelho/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Aparelho
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aparelhoList && aparelhoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Numero Serie</th>
                <th>Status</th>
                <th>Carga</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aparelhoList.map((aparelho, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/aparelho/${aparelho.id}`} color="link" size="sm">
                      {aparelho.id}
                    </Button>
                  </td>
                  <td>{aparelho.nome}</td>
                  <td>{aparelho.numeroSerie}</td>
                  <td>{aparelho.status}</td>
                  <td>{aparelho.carga}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/aparelho/${aparelho.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/aparelho/${aparelho.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/aparelho/${aparelho.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Aparelhos found</div>
        )}
      </div>
    </div>
  );
};

export default Aparelho;
