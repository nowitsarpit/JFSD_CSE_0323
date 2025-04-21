const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const Contact = require('./models/Contact');

const app = express();

// Middleware
app.use(cors({
  origin: 'http://localhost:3000', // React app's address
  credentials: true
}));
app.use(express.json());

// Connect to MongoDB - improved connection handling
mongoose.connect('mongodb://127.0.0.1:27017/phonebook', {
  useNewUrlParser: true,
  useUnifiedTopology: true
})
.then(() => console.log('✅ MongoDB Connected Successfully'))
.catch(err => console.log('❌ MongoDB Connection Error:', err));

// Routes
// Get all contacts
app.get('/api/contacts', async (req, res) => {
  try {
    const contacts = await Contact.find().sort({ name: 1 });
    console.log(`Found ${contacts.length} contacts`);
    res.json(contacts);
  } catch (err) {
    console.error('Error fetching contacts:', err);
    res.status(500).json({ error: 'Server Error', message: err.message });
  }
});

// Add a contact
app.post('/api/contacts', async (req, res) => {
  try {
    console.log('Received contact data:', req.body);
    const newContact = new Contact(req.body);
    const contact = await newContact.save();
    console.log('Contact saved:', contact);
    res.status(201).json(contact);
  } catch (err) {
    console.error('Error saving contact:', err);
    res.status(500).json({ error: 'Server Error', message: err.message });
  }
});

// Delete a contact
app.delete('/api/contacts/:id', async (req, res) => {
  try {
    const result = await Contact.findByIdAndDelete(req.params.id);
    console.log('Contact deleted:', result);
    res.json({ success: true });
  } catch (err) {
    console.error('Error deleting contact:', err);
    res.status(500).json({ error: 'Server Error', message: err.message });
  }
});

// Update a contact
app.put('/api/contacts/:id', async (req, res) => {
  try {
    console.log(`Updating contact ${req.params.id} with:`, req.body);
    const contact = await Contact.findByIdAndUpdate(
      req.params.id, 
      req.body,
      { new: true }
    );
    console.log('Contact updated:', contact);
    res.json(contact);
  } catch (err) {
    console.error('Error updating contact:', err);
    res.status(500).json({ error: 'Server Error', message: err.message });
  }
});

const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`🚀 Server running on port ${PORT}`));