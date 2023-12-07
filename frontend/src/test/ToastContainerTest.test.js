import React from 'react';
import { render } from '@testing-library/react';
import CustomToastContainer from '../components/ToastContainer';

describe('CustomToastContainer', () => {
  it('renders with specified props', () => {
   render(<CustomToastContainer />);
  });
});
