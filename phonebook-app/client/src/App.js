import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './styles.css';

// Base URL for API calls
const API_BASE_URL = 'http://localhost:5000/api';

function App() {
  const [contacts, setContacts] = useState([]);
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [birthDate, setBirthDate] = useState('');
  const [address, setAddress] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  // Show success message for 3 seconds
  const showSuccessMessage = (message) => {
    setSuccessMessage(message);
    setTimeout(() => setSuccessMessage(''), 3000);
  };

  // Fetch contacts from the server
  const fetchContacts = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const res = await axios.get(`${API_BASE_URL}/contacts`);
      console.log('Fetched contacts:', res.data);
      setContacts(res.data);
    } catch (err) {
      console.error('Error fetching contacts:', err);
      setError('Failed to load contacts. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  // Load contacts when component mounts
  useEffect(() => {
    fetchContacts();
  }, []);

  // Handle form submission (create/update contact)
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validate phone number - simple validation
    if (!/^\d{10}$/.test(phoneNumber.replace(/[^\d]/g, ''))) {
      setError('Please enter a valid 10-digit phone number');
      return;
    }
    
    const contactData = {
      name,
      phoneNumber,
      birthDate: birthDate || undefined,
      address
    };

    console.log('Submitting contact data:', contactData);
    setIsLoading(true);
    setError(null);
    
    try {
      if (editingId) {
        console.log(`Updating contact with ID: ${editingId}`);
        await axios.put(`${API_BASE_URL}/contacts/${editingId}`, contactData);
        showSuccessMessage('Contact updated successfully!');
        setEditingId(null);
      } else {
        console.log('Creating new contact');
        await axios.post(`${API_BASE_URL}/contacts`, contactData);
        showSuccessMessage('Contact added successfully!');
      }
      
      // Reset form
      setName('');
      setPhoneNumber('');
      setBirthDate('');
      setAddress('');
      
      // Refresh contacts
      fetchContacts();
    } catch (err) {
      console.error('Error saving contact:', err);
      setError(err.response?.data?.message || 'Failed to save contact. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  // Set up form for editing a contact
  const handleEdit = (contact) => {
    setEditingId(contact._id);
    setName(contact.name);
    setPhoneNumber(contact.phoneNumber);
    setBirthDate(contact.birthDate ? new Date(contact.birthDate).toISOString().split('T')[0] : '');
    setAddress(contact.address || '');
    
    // Scroll to form
    document.querySelector('.add-contact-container').scrollIntoView({ behavior: 'smooth' });
  };

  // Delete a contact
  const handleDelete = async (id, contactName) => {
    if (window.confirm(`Are you sure you want to delete ${contactName}?`)) {
      setIsLoading(true);
      try {
        await axios.delete(`${API_BASE_URL}/contacts/${id}`);
        showSuccessMessage('Contact deleted successfully!');
        fetchContacts();
      } catch (err) {
        console.error('Error deleting contact:', err);
        setError('Failed to delete contact. Please try again.');
        setIsLoading(false);
      }
    }
  };

  // Cancel editing
  const handleCancelEdit = () => {
    setEditingId(null);
    setName('');
    setPhoneNumber('');
    setBirthDate('');
    setAddress('');
  };

  // Filter contacts based on search term
  const filteredContacts = contacts.filter(contact => 
    contact.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    contact.phoneNumber.includes(searchTerm)
  );

  // Format phone number as user types
  const formatPhoneNumber = (value) => {
    const numbers = value.replace(/[^\d]/g, '');
    if (numbers.length <= 5) return numbers;
    return `${numbers.slice(0, 5)} ${numbers.slice(5, 10)}`;
  };

  return (
    <div className="container">
      <div className="header">
        <h1>📞 Phonebook</h1>
        <p className="subtitle">Store and manage your contacts with ease</p>
      </div>

      {successMessage && (
        <div className="success-message">
          {successMessage}
        </div>
      )}

      {error && (
        <div className="error-message">
          {error}
          <button onClick={() => setError(null)} className="close-btn">×</button>
        </div>
      )}

      <div className="search-container">
        <input
          type="text"
          placeholder="Search contacts by name or phone..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="search-input"
        />
      </div>

      <div className="content">
        <div className="add-contact-container">
          <h2>{editingId ? 'Edit Contact' : 'Add New Contact'}</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Name</label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Enter full name"
                required
                className="form-input"
              />
            </div>
            <div className="form-group">
              <label>Phone Number</label>
              <input
                type="text"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(formatPhoneNumber(e.target.value))}
                placeholder="99999 99999"
                required
                className="form-input"
              />
            </div>
            <div className="form-group">
              <label>Birth Date</label>
              <input
                type="date"
                value={birthDate}
                onChange={(e) => setBirthDate(e.target.value)}
                className="form-input"
              />
            </div>
            <div className="form-group">
              <label>Address</label>
              <input
                type="text"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                placeholder="Enter address"
                className="form-input"
              />
            </div>
            <div className="form-buttons">
              <button type="submit" className="submit-btn" disabled={isLoading}>
                {isLoading ? 'Processing...' : (editingId ? 'Update Contact' : 'Save Contact')}
              </button>
              {editingId && (
                <button type="button" onClick={handleCancelEdit} className="cancel-btn">
                  Cancel
                </button>
              )}
            </div>
          </form>
        </div>

        <div className="contacts-container">
          <h2>My Contacts ({filteredContacts.length})</h2>
          
          {isLoading && !contacts.length ? (
            <div className="loading">Loading contacts...</div>
          ) : filteredContacts.length === 0 ? (
            <div className="no-contacts">
              {searchTerm ? 'No contacts match your search' : 'No contacts yet. Add your first contact!'}
            </div>
          ) : (
            <div className="contacts-grid">
              {filteredContacts.map(contact => (
                <div key={contact._id} className="contact-card">
                  <div className="contact-avatar">
                    {contact.name.charAt(0).toUpperCase()}
                  </div>
                  <div className="contact-info">
                    <h3>{contact.name}</h3>
                    <p><span className="info-label">📱</span> {contact.phoneNumber}</p>
                    {contact.birthDate && (
                      <p><span className="info-label">🎂</span> {new Date(contact.birthDate).toLocaleDateString()}</p>
                    )}
                    {contact.address && (
                      <p><span className="info-label">📍</span> {contact.address}</p>
                    )}
                  </div>
                  <div className="contact-actions">
                    <button 
                      onClick={() => handleEdit(contact)} 
                      className="edit-btn"
                    >
                      Edit
                    </button>
                    <button 
                      onClick={() => handleDelete(contact._id, contact.name)} 
                      className="delete-btn"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
      
      <footer className="footer">
        <p>Interactive Phonebook App © {new Date().getFullYear()}</p>
      </footer>
    </div>
  );
}

export default App;