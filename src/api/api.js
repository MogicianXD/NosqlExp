import axios from 'axios';
import qs from 'qs'

axios.defaults.baseURL = '/NosqlExp'
let http = axios.create({
  timeout: 20000
});

export default {
  getStudent ( data ) {
    return http.get('/student', {'params': data});
  },

  login (id) {
    let params = {sid: id};
    return http.get('/login', {'params': params});
  },

  postStudent ( data ) {
    return http.post('/student', qs.stringify(data));
  }, 
  
  getCourse ( data ) {
    return http.get('/course', {'params': data});
  },

  postCourse ( data ) {
    return http.post('/course', qs.stringify(data));
  }, 
  
  getTeacher ( data ) {
    return http.get('/teacher', {'params': data});
  },

  postTeacher ( data ) {
    return http.post('/teacher', qs.stringify(data));
  }, 

  delete ( data ) {
    return http.post('/delete', qs.stringify(data));
  },
  
  query (id) {
    let params = {sid: id};
    return http.get('/elect', {'params': params})
  },

  queryAll () {
    return http.get('/course')
  },

  elect (id1, id2) {
    let data = {sid: id1, cid: id2, elect: true};
    return http.post('/elect', qs.stringify(data));
  },

  cancel (id1, id2) {
    let data = {sid: id1, cid: id2}
    return http.post('/elect', qs.stringify(data));
  }, 

  analysis (val) {
    let params = {tag: val};
    return http.get('/analysis', {'params': params})
  }
  
};