const { ExpansionPanelActions } = require("@material-ui/core");

describe('test', () => {
  it('test', () => {
    const mockSend = jest.fn(city => ({
      city: city
    }));

    const mockRes = jest.fn(() => ({
      send: mockSend
    }));

    const data = await location.showLocation(null, mockRes, null)
    expect(mockSend).toHaveBeenCalledWith('Burzaco1')
  })
})