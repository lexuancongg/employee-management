import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import { ReactNode } from 'react';

type DialogProps = {
  isShow: boolean;
  title?: string;
  children: ReactNode;

  okText?: string;
  cancelText?: string;
  loading?: boolean;

  ok: () => void;
  cancel: () => void;
};

export default function ConfirmationDialog({
  isShow,
  title,
  children,
  okText,
  cancelText,
  loading,
  ok,
  cancel,
}: DialogProps) {
  return (
    <Modal show={isShow} onHide={!loading ? cancel : undefined}>
      <Modal.Header closeButton>
        <Modal.Title>{title}</Modal.Title>
      </Modal.Header>

      <Modal.Body>{children}</Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={cancel} disabled={loading}>
          {cancelText || 'Cancel'}
        </Button>

        <Button variant="primary" onClick={ok} disabled={loading}>
          {loading ? 'Loading...' : okText || 'OK'}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}